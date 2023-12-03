package com.ruijie.scheduler.model;


import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Sonar {
    private  String mode;

    private  String  dockerFilePath;

    private  String nativeFilePath;

    public void setMode(@NonNull String mode) {
        this.mode = mode;
    }

    public void setDockerFilePath(@NonNull String dockerFilePath) {
        this.dockerFilePath = dockerFilePath;
    }

    public void setNativeFilePath(@NonNull String nativeFilePath) {
        this.nativeFilePath = nativeFilePath;
    }

    public String getMode() {
        return this.mode;
    }


    public String getDockerFilePath() {
       return this.dockerFilePath;
    }

    public String getNativeFilePath() {
        return this.nativeFilePath;
    }

    @Override
    public String toString() {
        return "Sonar{" +
                "mode='" + mode + '\'' +
                ", dockerFilePath='" + dockerFilePath + '\'' +
                ", nativeFilePath='" + nativeFilePath + '\'' +
                '}';
    }
}
