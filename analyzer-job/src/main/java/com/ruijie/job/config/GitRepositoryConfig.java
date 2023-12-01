package com.ruijie.job.config;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "git")
public class GitRepositoryConfig {
    private  String privateKeyFilePath;
    public void setPrivateKeyFilePath(@NonNull String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }
    public String getPrivateKeyFilePath() {
        return  this.privateKeyFilePath;
    }
    @Override
    public String toString() {
        return "GitRepositoryConfig{" +
                "privateKeyFilePath='" + privateKeyFilePath + '\'' +
                '}';
    }
}
