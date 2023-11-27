package org.ruijie.core.docker;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

public class DockerClientConfig {
    private String host;
    private String apiVersion;

    public String getHost() {
        return this.host;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public void setHost(@NonNull String host) {
        this.host = host;

    }

    public void setApiVersion(@NonNull String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public String toString() {
        return "DockerClientConfig{" +
                "host='" + host + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                '}';
    }
}
