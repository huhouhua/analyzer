package com.ruijie.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerConfig {
    private  Integer maxParallel;
    private  Integer containerRunningCount;
    private  long jobWaitTimeSecond;

    public  void  setMaxParallel(Integer maxParallel){
        this.maxParallel = maxParallel;
    }
    public  Integer  getMaxParallel(){
        return  this.maxParallel;
    }
    public  void  setJobWaitTimeSecond(long jobWaitTimeSecond){
        this.jobWaitTimeSecond = jobWaitTimeSecond;
    }
    public  long  getJobWaitTimeSecond(){
        return  jobWaitTimeSecond;
    }

    public   Integer getContainerRunningCount(){
        return  this.containerRunningCount;
    }
    public void  setContainerRunningCount(Integer containerRunningCount){
        this.containerRunningCount = containerRunningCount;
    }
    @Override
    public String toString() {
        return "SchedulerConfig{" +
                "maxParallel=" + maxParallel +
                ", jobWaitTimeSecond=" + jobWaitTimeSecond +
                ", containerRunningCount=" + containerRunningCount +
                '}';
    }
}
