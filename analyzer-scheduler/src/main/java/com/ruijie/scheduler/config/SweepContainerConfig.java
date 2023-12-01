package com.ruijie.scheduler.config;

import cn.hutool.core.util.ReUtil;

import java.util.Arrays;

public class SweepContainerConfig {

    private  Integer timeoutHours;

    private  String timeCron;
    private  String[] matches;

    public Integer getTimeoutHours() {
        return timeoutHours;
    }

    public void setTimeoutHours(Integer timeoutHours) {
        this.timeoutHours = timeoutHours;
    }

    public String getTimeCron() {
        return timeCron;
    }

    public void setTimeCron(String timeCron) {
        this.timeCron = timeCron;
    }

    public String[] getMatches() {
        return matches;
    }

    public void setMatches(String[] matches) {
        this.matches = matches;
    }


    public  boolean match(String value){
        return Arrays.stream(matches).anyMatch(match-> ReUtil.isMatch(match,value));
    }

    @Override
    public String toString() {
        return "SweepContainerConfig{" +
                "timeoutHours=" + timeoutHours +
                ", timeCron='" + timeCron + '\'' +
                ", matches=" + Arrays.toString(matches) +
                '}';
    }
}
