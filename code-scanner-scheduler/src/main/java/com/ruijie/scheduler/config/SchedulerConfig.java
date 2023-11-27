package com.ruijie.scheduler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ruijie.scheduler.factory.AutowireSpringBeanJobFactory;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.concurrent.Executor;


@Configuration
public class SchedulerConfig {
    @Bean
    public ObjectMapper objectMapper() {
     return  new ObjectMapper( new YAMLFactory());
    }

    @Bean
    public Scheduler scheduler() {
        return this.schedulerFactory().
                getScheduler();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName("code_scanner");
        factory.setTaskExecutor(schedulerThreadPool());
        factory.setApplicationContextSchedulerContextKey("application");
        factory.setJobFactory(new AutowireSpringBeanJobFactory());
        factory.setStartupDelay(0);
        return factory;
    }

    @Bean
    public Executor schedulerThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors);
        executor.setQueueCapacity(processors);
        return executor;
    }

}
