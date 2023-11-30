package org.ruijie.core;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public class SonarConfig {

    private boolean enable;
    private  String url;
    private  String login;
    private  String password;
    private  String branch;

    public void setUrl(@NonNull String url) {
        this.url = url;
    }
    public void setLogin(@NonNull String login) {
        this.login = login;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setBranch(@NonNull String branch) {
        this.branch = branch;
    }

    public String getUrl() {
        return  this.url;
    }

    public String getLogin() {
       return this.login;
    }

    public String getPassword() {
        return  this.password;
    }

    public String getBranch() {
        return  this.branch;
    }


    public boolean getEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "SonarConfig{" +
                "enable=" + enable +
                ", url='" + url + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
