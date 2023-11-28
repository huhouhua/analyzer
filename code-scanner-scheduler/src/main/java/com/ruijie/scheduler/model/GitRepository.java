package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GitRepository {
    private String url;
    private String branch;
    private String taskFilePath;
    private  String privateKeyFilePath;
    public void setUrl(@NonNull String url) {
        this.url = url;
    }
    public void setPrivateKeyFilePath(@NonNull String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }
    public void setBranch(@NonNull String branch) {
        this.branch = branch;
    }

    public void setTaskFilePath(@NonNull String taskFilePath) {
        this.taskFilePath = taskFilePath;
    }

    public String getTaskFilePath() {
        return  this.taskFilePath;
    }
    public String getPrivateKeyFilePath() {
        return  this.privateKeyFilePath;
    }
    public String getUrl() {
        return this.url;
    }

    public String getBranch() {
        return this.branch;
    }

    @Override
    public String toString() {
        return "GitRepository{" +
                "url='" + url + '\'' +
                ", branch='" + branch + '\'' +
                ", taskFilePath='" + taskFilePath + '\'' +
                ", privateKeyFilePath='" + privateKeyFilePath + '\'' +
                '}';
    }
}
