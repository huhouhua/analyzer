package com.ruijie.scheduler.config;

import cn.hutool.core.util.ReUtil;

import java.util.Arrays;

public class SweepImageConfig {
    private  String timeCron;
    private  String[] matches;

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
    @Override
    public String toString() {
        return "SweepImageConfig{" +
                "timeCron='" + timeCron + '\'' +
                ", matches=" + Arrays.toString(matches) +
                '}';
    }
    public  boolean match(String value){
        return Arrays.stream(matches).anyMatch(match-> ReUtil.isMatch(match,value));
    }
}
