package com.ruijie.scheduler.config;

import com.ruijie.scheduler.model.RepositoryConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfigProvider {

    @Bean
    @ConfigurationProperties(prefix = "git-repository")
    public RepositoryConfig getConfig(){
        return  new RepositoryConfig();
    }


}
