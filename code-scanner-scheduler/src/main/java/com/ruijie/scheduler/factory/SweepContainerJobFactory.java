package com.ruijie.scheduler.factory;

import com.ruijie.scheduler.config.SweepContainerConfig;
import com.ruijie.scheduler.job.SweepContainerJob;
import lombok.NonNull;
import org.quartz.*;

public class SweepContainerJobFactory  implements JobProvider {
    private static final String SWEEP_JOB_KEY = "container";
    private static final String SWEEP_JOB_GROUP = "sweep-1";
    private static final String  TRIGGER_NAME   = "sweep-container";
    private final SweepContainerConfig sweepConfig;
    public SweepContainerJobFactory(@NonNull SweepContainerConfig sweepConfig) {
        this.sweepConfig = sweepConfig;
    }
    @Override
    public JobDetail createJob() {
        return JobBuilder.newJob(SweepContainerJob.class).
                withIdentity(SWEEP_JOB_KEY, SWEEP_JOB_GROUP).
                withDescription("clear mvc container").
                build();
    }

    @Override
    public Trigger createTrigger(@NonNull TriggerKey key) {
        return TriggerBuilder.newTrigger().
                withIdentity(key).
                withSchedule(CronScheduleBuilder.cronSchedule(sweepConfig.getTimeCron())).
                startNow().
                build();
    }

    @Override
    public  Trigger createDefaultTrigger(){
        return this.createTrigger(createTriggerKey());
    }

    @Override
    public  TriggerKey createTriggerKey(){
        return TriggerKey.triggerKey(TRIGGER_NAME,SWEEP_JOB_GROUP);
    }
}
