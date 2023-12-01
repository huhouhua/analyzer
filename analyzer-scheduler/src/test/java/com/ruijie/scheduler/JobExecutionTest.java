package com.ruijie.scheduler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.scheduler.job.SchedulerManager;
import com.ruijie.scheduler.model.TaskConfig;
import org.quartz.*;

import java.util.Date;

public  class  JobExecutionTest implements JobExecutionContext {

    private  final ObjectMapper objectMapper;
    public  JobExecutionTest(ObjectMapper mapper){
        this.objectMapper =mapper;
    }

    @Override
    public Scheduler getScheduler() {
        return null;
    }

    @Override
    public Trigger getTrigger() {
        return null;
    }

    @Override
    public Calendar getCalendar() {
        return null;
    }

    @Override
    public boolean isRecovering() {
        return false;
    }

    @Override
    public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
        return null;
    }

    @Override
    public int getRefireCount() {
        return 0;
    }

    @Override
    public JobDataMap getMergedJobDataMap() {
        return null;
    }

    @Override
    public JobDetail getJobDetail() {
        return  new JobDetail() {
            @Override
            public JobKey getKey() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Class<? extends Job> getJobClass() {
                return null;
            }

            @Override
            public JobDataMap getJobDataMap() {
                JobDataMap jobDetail=  new JobDataMap();
                String content = FileUtil.readUtf8String(TestContract.SONAR_CONFIG_FILE);
                try {
                    TaskConfig config =  objectMapper.readValue(content, TaskConfig.class);
                    jobDetail.put(SchedulerManager.TASK_KEY, JSONUtil.toJsonStr(config));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return  jobDetail;
            }
            @Override
            public boolean isDurable() {
                return false;
            }

            @Override
            public boolean isPersistJobDataAfterExecution() {
                return false;
            }

            @Override
            public boolean isConcurrentExectionDisallowed() {
                return false;
            }

            @Override
            public boolean requestsRecovery() {
                return false;
            }

            @Override
            public JobBuilder getJobBuilder() {
                return null;
            }

            @Override
            public Object clone() {
                return null;
            }
        };
    }

    @Override
    public Job getJobInstance() {
        return null;
    }

    @Override
    public Date getFireTime() {
        return null;
    }

    @Override
    public Date getScheduledFireTime() {
        return null;
    }

    @Override
    public Date getPreviousFireTime() {
        return null;
    }

    @Override
    public Date getNextFireTime() {
        return null;
    }

    @Override
    public String getFireInstanceId() {
        return null;
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void setResult(Object o) {

    }

    @Override
    public long getJobRunTime() {
        return 0;
    }

    @Override
    public void put(Object o, Object o1) {

    }

    @Override
    public Object get(Object o) {
        return null;
    }
}
