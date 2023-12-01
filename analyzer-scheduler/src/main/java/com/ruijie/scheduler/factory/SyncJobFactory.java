package com.ruijie.scheduler.factory;

import com.ruijie.scheduler.job.SyncRepositoryJob;
import com.ruijie.scheduler.model.RepositoryConfig;
import lombok.NonNull;
import org.quartz.*;

public class SyncJobFactory  implements JobProvider {
    private static final String SYNC_REPO_JOB_KEY = "sync-riil-sonar";
    private static final String SYNC_REPO_JOB_GROUP = "sync";
    private static final String  TRIGGER_NAME   = "git-repo";
    private final RepositoryConfig repositoryConfig;

    public SyncJobFactory(@NonNull RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
    @Override
    public JobDetail createJob() {
        return JobBuilder.newJob(SyncRepositoryJob.class).
                withIdentity(SYNC_REPO_JOB_KEY, SYNC_REPO_JOB_GROUP).
                withDescription("sync git repository").
                build();
    }

    @Override
    public Trigger createTrigger(@NonNull TriggerKey key) {
        return TriggerBuilder.newTrigger().
                withIdentity(key).
                withSchedule(CronScheduleBuilder.cronSchedule(repositoryConfig.getSyncTimeCron())).
                startNow().
                build();
    }

    @Override
    public  Trigger createDefaultTrigger(){
        return this.createTrigger(createTriggerKey());
    }

    @Override
    public  TriggerKey createTriggerKey(){
        return TriggerKey.triggerKey(TRIGGER_NAME,SYNC_REPO_JOB_GROUP);
    }
}
