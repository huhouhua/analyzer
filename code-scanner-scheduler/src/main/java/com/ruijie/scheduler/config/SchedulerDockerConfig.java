package com.ruijie.scheduler.config;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import org.ruijie.core.docker.DockerClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "docker")
public class SchedulerDockerConfig extends DockerClientConfig {
    private  String imageTag;
    private  String imageRepository;
    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(@NonNull String imageTag) {
        this.imageTag = imageTag;
    }

    public String getImageRepository() {
        return imageRepository;
    }

    public void setImageRepository(@NonNull String imageRepository) {
        this.imageRepository = imageRepository;
    }

    public  String  getImage(){
        return StrUtil.format("{}:{}",this.getImageRepository(),this.getImageTag());
    }
    @Override
    public String toString() {
        return "SchedulerDockerConfig{" +
                "imageTag='" + imageTag + '\'' +
                ", imageRepository='" + imageRepository + '\'' +
                '}';
    }

}
