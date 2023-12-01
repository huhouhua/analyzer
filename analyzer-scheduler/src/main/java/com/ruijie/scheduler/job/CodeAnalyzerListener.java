package com.ruijie.scheduler.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CodeAnalyzerListener implements JobListener {

    private final Logger LOG = LoggerFactory.getLogger(CodeAnalyzerListener.class.getName());
    @Override
    public String getName() {
        return CodeAnalyzerListener.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException e) {
        String taskStr = SchedulerManager.getTaskConfig();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        jobDataMap.replace(SchedulerManager.TASK_KEY,taskStr);
        LOG.info("update job data.....");
    }
}
