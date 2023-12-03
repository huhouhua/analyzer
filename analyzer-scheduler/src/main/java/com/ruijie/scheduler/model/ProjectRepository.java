package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProjectRepository {
    private  String url;
    private  String  branch;
    private  String  sonarFilePath;

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setSonarFilePath( String sonarFilePath) {
        this.sonarFilePath = sonarFilePath;
    }
    public String getUrl() {
        return this.url;
    }

    public String getBranch() {
        return  this.branch;
    }
    public String getSonarFilePath() {
        return this.sonarFilePath;
    }


    @Override
    public String toString() {
        return "ProjectRepository{" +
                "url='" + url + '\'' +
                ", branch='" + branch + '\'' +
                ", sonarFilePath='" + sonarFilePath + '\'' +
                '}';
    }
}
