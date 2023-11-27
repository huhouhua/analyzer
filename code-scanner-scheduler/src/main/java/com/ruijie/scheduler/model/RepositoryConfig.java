package com.ruijie.scheduler.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class RepositoryConfig {
    private  String syncTimeCron;

    private  String storeDirectory;

    private  GitRepository repository;

    public String getSyncTimeCron() {
        return syncTimeCron;
    }

    public void setSyncTimeCron(@NonNull String syncTimeCron) {
        this.syncTimeCron = syncTimeCron;
    }

    public String getStoreDirectory() {
        return storeDirectory;
    }

    public void setStoreDirectory(@NonNull String storeDirectory) {
        this.storeDirectory = storeDirectory;
    }

    public GitRepository getRepository() {
        return repository;
    }

    public void setRepository( @NonNull GitRepository repository) {
        this.repository = repository;
    }

    @Override
    public String toString() {
        return "RepositoryConfig{" +
                "syncTimeCron='" + syncTimeCron + '\'' +
                ", storeDirectory='" + storeDirectory + '\'' +
                ", repository=" + repository +
                '}';
    }

}
