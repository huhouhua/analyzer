package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProjectRepository {
    private  String url;
    private  String  branch;
    private  String  sonarFileUrl;

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setSonarFileUrl( String sonarFileUrl) {
        this.sonarFileUrl = sonarFileUrl;
    }
    public String getUrl() {
        return this.url;
    }

    public String getBranch() {
        return  this.branch;
    }
    public String getSonarFileUrl() {
        return this.sonarFileUrl;
    }


    @Override
    public String toString() {
        return "ProjectRepository{" +
                "url='" + url + '\'' +
                ", branch='" + branch + '\'' +
                ", sonarFileUrl='" + sonarFileUrl + '\'' +
                '}';
    }
}
