package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ruijie.scheduler.model.TaskConfig;
import lombok.NonNull;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class SchedulerManager {
    private static final String  TRIGGER_NAME   = "scan-repo";
    private static final String JOB_NAME = "scan";
    private static final String GROUP = "code";
    public static final String TASK_KEY = "task_config";
    private final Logger LOG = LoggerFactory.getLogger(SchedulerManager.class.getName());
    private final Scheduler scheduler;
    private final JobKey jobKey;
    private final TriggerKey triggerKey;
    private final CurrentTriggerCron currentTriggerCron;
    private static String taskConfigStr;

    @Autowired
    public SchedulerManager(Scheduler scheduler) throws SchedulerException {
        this.scheduler = scheduler;
        this.jobKey = JobKey.jobKey(JOB_NAME, GROUP);
        this.triggerKey = TriggerKey.triggerKey(TRIGGER_NAME, GROUP);
        this.currentTriggerCron = CurrentTriggerCron.newInstance();
        this.scheduler.getListenerManager().
                addJobListener(new CodeScanListener(), KeyMatcher.keyEquals(jobKey));
    }

    public void set(@NonNull TaskConfig taskConfig) throws SchedulerException {
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        taskConfigStr = JSONUtil.toJsonStr(taskConfig);

        String timeCron = taskConfig.getGlobal().getTriggerTimeCron();
        if (trigger == null) {
            LOG.info(StrUtil.format("start schedule job! cron set is {}!", timeCron));
            this.scheduleJob(taskConfigStr, timeCron);
        } else {
            this.updateJobCron(trigger, timeCron);
        }
        currentTriggerCron.set(timeCron);
    }

    public static String getTaskConfig() {
        return taskConfigStr;
    }

    private JobDetail createJobFromBase(JobDetail job, String taskStr) {
        return job.
                getJobBuilder().
                ofType(CodeScanJob.class).
                usingJobData(TASK_KEY, taskStr).
                build();
    }

    private CronTrigger createTriggerFromBase(CronTrigger trigger, String timeCron) {
        return trigger.getTriggerBuilder().
                withSchedule(CurrentTriggerCron.fromCronSchedule(timeCron)).
                build();
    }

    private JobDetail createJob(String taskStr) {
        return JobBuilder.newJob(CodeScanJob.class).
                withIdentity(jobKey).
                usingJobData(TASK_KEY, taskStr).
                withDescription("code git repository").
                build();
    }

    private CronTrigger createTrigger(String timeCron) {
        return TriggerBuilder.newTrigger().
                withIdentity(triggerKey).
                withSchedule(CurrentTriggerCron.fromCronSchedule(timeCron)).
                startAt(cronToDateTime(timeCron)).
                build();
    }

    private void scheduleJob(String taskStr, String timeCron) {
        try {
            JobDetail jobDetail = this.createJob(taskStr);
            CronTrigger trigger = this.createTrigger(timeCron);
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateJobCron(CronTrigger trigger, String timeCron) {
        try {
//            JobDetail newJobDetail = this.createJobFromBase(scheduler.getJobDetail(jobKey),taskStr);
            if (currentTriggerCron.equals(timeCron)) {
                return;
            }
            LOG.info(StrUtil.format("trigger cron change from {} to {}!", currentTriggerCron.get(), timeCron));
            CronTrigger newTrigger = this.createTriggerFromBase(trigger, timeCron);
            scheduler.rescheduleJob(triggerKey, newTrigger);

//            LOG.info("update job data.....");
//            scheduler.addJob(newJobDetail,true,false);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private  Date  cronToDateTime(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            Date nextDateTime = cron.getNextValidTimeAfter(new Date());
            LOG.info(StrUtil.format("start execution time： {}", nextDateTime));
            return nextDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(StrUtil.format("transform cron expression {} fail!",e.getMessage()));
        }
        return new Date();
    }

}
