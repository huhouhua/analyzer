package com.ruijie.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "notification")
public class NotificationConfig {
    public  String  webhook;

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    @Override
    public String toString() {
        return "NotificationConfig{" +
                "webhook='" + webhook + '\'' +
                '}';
    }


    
}
