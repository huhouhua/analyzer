package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.ScheduleBuilder;

class CurrentTriggerCron {
    private String currentTimeCron;
    public static CurrentTriggerCron newInstance() {
        return new CurrentTriggerCron();
    }
    public void set(@NonNull String timeCron) {
        this.currentTimeCron = timeCron;
    }
    public String get() {
        return currentTimeCron;
    }
    public  static  ScheduleBuilder<CronTrigger> fromCronSchedule(@NonNull String timeCron){
        return  CronScheduleBuilder.cronSchedule(timeCron);
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        return StrUtil.equals(currentTimeCron, (String) obj);
    }
}
