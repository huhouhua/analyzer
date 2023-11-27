package com.ruijie.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sweep")
public class SweepConfig {
    private  SweepContainerConfig container;

    private  SweepImageConfig image;

    public SweepContainerConfig getContainer() {
        return container;
    }

    public void setContainer(SweepContainerConfig container) {
        this.container = container;
    }

    public SweepImageConfig getImage() {
        return image;
    }

    public void setImage(SweepImageConfig image) {
        this.image = image;
    }
    @Override
    public String toString() {
        return "SweepConfig{" +
                "container=" + container +
                ", image=" + image +
                '}';
    }
}
