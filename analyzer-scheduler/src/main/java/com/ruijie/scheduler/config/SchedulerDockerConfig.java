package com.ruijie.scheduler.config;

import com.ruijie.core.docker.DockerClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "docker")
public class SchedulerDockerConfig extends DockerClientConfig {

}
