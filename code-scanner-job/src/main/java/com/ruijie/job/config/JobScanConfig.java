package com.ruijie.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "store")
public class JobScanConfig {
    private  String directory;
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
    @Override
    public String toString() {
        return "JobConfig{" +
                "directory='" + directory + '\'' +
                '}';
    }
}
