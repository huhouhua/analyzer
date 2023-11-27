package com.ruijie.scheduler.model;


import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Sonar {
    private  String username;
    private  String  password;

    private  String  dockerFileUrl;

    private  String nativeFileUrl;


    public void setUsername(@NonNull  String username) {
        this.username = username;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setDockerFileUrl(@NonNull String dockerFileUrl) {
        this.dockerFileUrl = dockerFileUrl;
    }

    public void setNativeFileUrl(@NonNull String nativeFileUrl) {
        this.nativeFileUrl = nativeFileUrl;
    }

    public String getUsername() {
       return this.username;
    }

    public String getPassword() {
        return  this.password;
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
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dockerFileUrl='" + dockerFileUrl + '\'' +
                ", nativeFileUrl='" + nativeFileUrl + '\'' +
                '}';
    }

}
