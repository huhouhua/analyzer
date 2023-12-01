package com.ruijie.scheduler;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartApplicationListener  implements ApplicationListener<ContextRefreshedEvent> {
    private  final Logger LOG =  LoggerFactory.getLogger(StartApplicationListener.class.getName());
    private  final SchedulerProcess schedulerProcess;

    @Autowired
    public StartApplicationListener(SchedulerProcess schedulerProcess ){
        this.schedulerProcess =schedulerProcess;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            LOG.info("start scheduler");
            schedulerProcess.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
