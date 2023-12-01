package com.ruijie.scheduler.factory;

import com.ruijie.scheduler.config.SweepImageConfig;
import com.ruijie.scheduler.job.SweepImageJob;
import lombok.NonNull;
import org.quartz.*;

public class SweepImageJobFactory  implements  JobProvider{
    private static final String SWEEP_JOB_KEY = "image";
    private static final String SWEEP_JOB_GROUP = "sweep-2";
    private static final String  TRIGGER_NAME   = "sweep-image";
    private final SweepImageConfig sweepConfig;
    public SweepImageJobFactory(@NonNull SweepImageConfig sweepConfig) {
        this.sweepConfig = sweepConfig;
    }
    @Override
    public JobDetail createJob() {
        return JobBuilder.newJob(SweepImageJob.class).
                withIdentity(SWEEP_JOB_KEY, SWEEP_JOB_GROUP).
                withDescription("clear image container").
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
