package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GitRepository {
    private String url;
    private String branch;
    private String taskFilePath;
    private String userName;
    private String password;

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setBranch(@NonNull String branch) {
        this.branch = branch;
    }

    public void setTaskFilePath(@NonNull String taskFilePath) {
        this.taskFilePath = taskFilePath;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getTaskFilePath() {
        return  this.taskFilePath;
    }

    public String getPassword() {
        return  this.password;
    }
    public String getUrl() {
        return this.url;
    }

    public String getBranch() {
        return this.branch;
    }

    public String getUserName() {
        return  this.userName;
    }
    @Override
    public String toString() {
        return "GitRepository{" +
                "url='" + url + '\'' +
                ", branch='" + branch + '\'' +
                ", taskFilePath='" + taskFilePath + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
