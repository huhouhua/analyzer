package com.ruijie.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sweep")
public class SweepConfig {
    private  MatchExpressionsConfig matchExpressions;

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

    public MatchExpressionsConfig getMatchExpressions() {
        return this.matchExpressions;
    }
    public void setMatchExpressions(MatchExpressionsConfig matchExpressions) {
        this.matchExpressions = matchExpressions;
    }
    @Override
    public String toString() {
        return "SweepConfig{" +
                "matchExpressions=" + matchExpressions +
                ", container=" + container +
                ", image=" + image +
                '}';
    }
}
