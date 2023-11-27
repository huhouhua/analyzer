package com.ruijie.scheduler.model;

import org.springframework.stereotype.Component;

@Component
public class GlobalRepository {

    private  String branch;


    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "GlobalRepository{" +
                "branch='" + branch + '\'' +
                '}';
    }
}
