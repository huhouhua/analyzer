package com.ruijie.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "default.project")
public class ProjectConfigProvider {
    private  String name;
    private String url;
    private String branch;
    private String sonarFilePath;
    private String mode;
    private String description;

    public String getUrl() {
        return this.url;
    }

    public String getName() {
        return this.name;
    }

    public String getBranch() {
        return this.branch;
    }

    public String getSonarFilePath() {
        return this.sonarFilePath;
    }

    public String getMode() {
        return this.mode;
    }

    public String getDescription() {
        return this.description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setSonarFilePath(String sonarFilePath) {
        this.sonarFilePath = sonarFilePath;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProjectConfigProvider{" +
                "url='" + url + '\'' +
                ", branch='" + branch + '\'' +
                ", sonarFilePath='" + sonarFilePath + '\'' +
                ", mode='" + mode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
