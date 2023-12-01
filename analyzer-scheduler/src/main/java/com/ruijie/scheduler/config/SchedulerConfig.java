package com.ruijie.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerConfig {
    private  Integer maxParallel;
    private  long maxJobWaitTimeSecond;

    public  void  setMaxParallel(Integer maxParallel){
        this.maxParallel = maxParallel;
    }
    public  Integer  getMaxParallel(){
        return  this.maxParallel;
    }
    public  void  setMaxJobWaitTimeSecond(long maxJobWaitTimeSecond){
        this.maxJobWaitTimeSecond = maxJobWaitTimeSecond;
    }
    public  long  getMaxJobWaitTimeSecond(){
        return  maxJobWaitTimeSecond;
    }

    @Override
    public String toString() {
        return "SchedulerConfig{" +
                "maxParallel=" + maxParallel +
                ", maxJobWaitTimeSecond=" + maxJobWaitTimeSecond +
                '}';
    }
}
