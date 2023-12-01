package com.ruijie.scheduler.model;


import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Sonar {
    private  String mode;

    private  String  dockerFileUrl;

    private  String nativeFileUrl;

    public void setMode(@NonNull String mode) {
        this.mode = mode;
    }

    public void setDockerFileUrl(@NonNull String dockerFileUrl) {
        this.dockerFileUrl = dockerFileUrl;
    }

    public void setNativeFileUrl(@NonNull String nativeFileUrl) {
        this.nativeFileUrl = nativeFileUrl;
    }

    public String getMode() {
        return this.mode;
    }


    public String getDockerFileUrl() {
       return this.dockerFileUrl;
    }

    public String getNativeFileUrl() {
        return this.nativeFileUrl;
    }

    @Override
    public String toString() {
        return "Sonar{" +
                "mode='" + mode + '\'' +
                ", dockerFileUrl='" + dockerFileUrl + '\'' +
                ", nativeFileUrl='" + nativeFileUrl + '\'' +
                '}';
    }
}
