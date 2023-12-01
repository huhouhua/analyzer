package com.ruijie.scheduler.config;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "job")
public class JobConfig {
    private  String tag;
    private  String repository;

    private  String notifyWebhook;

    public String getNotifyWebhook() {
        return notifyWebhook;
    }

    public void setNotifyWebhook(@NonNull String notifyWebhook) {
        this.notifyWebhook = notifyWebhook;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(@NonNull String repository) {
        this.repository = repository;
    }

    public  String  toImage(){
        return StrUtil.format("{}:{}",this.getRepository(),this.getTag());
    }

    @Override
    public String toString() {
        return "JobConfig{" +
                "tag='" + tag + '\'' +
                ", repository='" + repository + '\'' +
                ", notifyWebhook='" + notifyWebhook + '\'' +
                '}';
    }
}
