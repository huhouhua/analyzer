package com.ruijie.scheduler.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CodeScanListener implements JobListener {

    private final Logger LOG = LoggerFactory.getLogger(CodeScanListener.class.getName());
    @Override
    public String getName() {
        return CodeScanListener.class.getName();
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
