package com.ruijie.job.config;

import com.ruijie.core.docker.DockerClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "docker")
public class JobDockerConfig  extends DockerClientConfig {


}
