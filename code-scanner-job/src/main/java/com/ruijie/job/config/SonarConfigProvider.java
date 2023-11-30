package com.ruijie.job.config;

import org.ruijie.core.SonarConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "default.sonar")
public class SonarConfigProvider extends SonarConfig {

}
