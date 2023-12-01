package com.ruijie.scheduler.config;

import com.ruijie.core.SonarConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sonar")
public class SonarConfigProvider extends SonarConfig {
    
}
