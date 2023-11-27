package com.ruijie.job.middleware;
import lombok.NonNull;
import org.ruijie.core.Context;
public class ScanContext implements Context {
    private  String repoName;
    private  String repoUrl;
    private  String branch;
    private  String sonarFileUrl;
    private  String mode;
    private  String description;
    public ScanContext(
            @NonNull String repoName,
            @NonNull String repoUrl,
            @NonNull String branch,
                       @NonNull String sonarFileUrl,
                       @NonNull String mode,
                       @NonNull String description) {
        this.repoName =repoName;
        this.repoUrl = repoUrl;
        this.branch = branch;
        this.sonarFileUrl = sonarFileUrl;
        this.mode = mode;
        this.description = description;
    }

    public String getBranch() {
        return branch;
    }

    public String getSonarFileUrl() {
        return sonarFileUrl;
    }

    public String getMode() {
        return mode;
    }

    public String getDescription() {
        return description;
    }

    public String getRepoUrl() {
        return repoUrl;
    }
    public String getRepoName() {
        return repoName;
    }

    @Override
    public String toString() {
        return "ScanContext{" +
                "repoName='" + repoName + '\'' +
                "repoUrl='" + repoUrl + '\'' +
                "branch='" + branch + '\'' +
                ", sonarFileUrl='" + sonarFileUrl + '\'' +
                ", mode='" + mode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
