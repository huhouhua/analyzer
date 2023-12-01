package com.ruijie.scheduler;

import com.ruijie.scheduler.config.RepositoryConfigProvider;
import com.ruijie.scheduler.config.SweepConfig;
import com.ruijie.scheduler.factory.JobProvider;
import com.ruijie.scheduler.factory.SweepContainerJobFactory;
import com.ruijie.scheduler.factory.SweepImageJobFactory;
import com.ruijie.scheduler.factory.SyncJobFactory;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SchedulerProcess {
    private final org.quartz.Scheduler scheduler;
    private final List<JobProvider> jobProviderList = new ArrayList<>();

    @Autowired
    public SchedulerProcess(org.quartz.Scheduler scheduler,
                            SweepConfig sweepConfig,
                            RepositoryConfigProvider provider) throws FileNotFoundException {
        this.scheduler = scheduler;
        this.initialize(sweepConfig, provider);
    }

    public void start() throws SchedulerException {
        try {
            this.prepare();
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void prepare() throws SchedulerException {
        for (JobProvider provider : jobProviderList) {
            TriggerKey triggerKey = provider.createTriggerKey();
            Trigger trigger = scheduler.getTrigger(triggerKey);
            scheduler.scheduleJob(provider.createJob(),
                    trigger == null ? provider.createTrigger(triggerKey) : trigger);
        }
    }

    private void initialize(SweepConfig sweepConfig, RepositoryConfigProvider provider) throws FileNotFoundException {
        jobProviderList.add(new SyncJobFactory(provider.getConfig()));
        jobProviderList.add(new SweepContainerJobFactory(sweepConfig.getContainer()));
        jobProviderList.add(new SweepImageJobFactory(sweepConfig.getImage()));
        RepositoryConfigProvider.initGitConfig(provider.getConfig().getRepository());
    }
}
