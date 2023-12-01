package com.ruijie.scheduler.factory;

import lombok.NonNull;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public interface JobFactory {

    JobDetail createJob();

    Trigger createTrigger(@NonNull TriggerKey key);

    Trigger createDefaultTrigger();

    TriggerKey createTriggerKey();
}
