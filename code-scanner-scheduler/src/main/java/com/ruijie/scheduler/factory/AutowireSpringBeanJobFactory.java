package com.ruijie.scheduler.factory;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AutowireSpringBeanJobFactory implements JobFactory, ApplicationContextAware {
    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        beanFactory = context.getAutowireCapableBeanFactory();
    }
    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler){
        final Job job = beanFactory.createBean(triggerFiredBundle.getJobDetail().getJobClass());
        beanFactory.autowireBean(job);
        return job;
    }
}
