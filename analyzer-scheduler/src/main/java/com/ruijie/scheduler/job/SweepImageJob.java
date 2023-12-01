package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.model.Image;
import com.ruijie.core.docker.DockerClientWrapper;
import com.ruijie.scheduler.config.SchedulerDockerConfig;
import com.ruijie.scheduler.config.SweepConfig;
import com.ruijie.scheduler.config.SweepImageConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SweepImageJob implements Job {
    private final Logger LOG = LoggerFactory.getLogger(SweepImageJob.class.getName());
    private final DockerClientWrapper dockerClientWrapper;
    private final SweepImageConfig sweepConfig;
    private final SweepCondition sweepCondition;

    @Autowired
    public SweepImageJob(SchedulerDockerConfig dockerConfig,
                         SweepConfig sweepConfig) {
        this.sweepConfig = sweepConfig.getImage();
        this.dockerClientWrapper = DockerClientWrapper.newWrapper(dockerConfig);
        this.sweepCondition = new SweepCondition(dockerClientWrapper, sweepConfig.getMatchExpressions());
    }

    @Override
    public void execute(JobExecutionContext context) {
        if (sweepCondition.match()) {
            return;
        }
        List<Image> imageList = dockerClientWrapper.getImageList();
        for (Image image : imageList) {
            if (image.getContainers() > 0) {
                continue;
            }
            if (image.getRepoTags().length == 0 || sweepConfig.match(image.getRepoTags()[0])) {
                try {
                    dockerClientWrapper.removeImage(image.getId(), false);
                    LOG.info(StrUtil.format("remove image {} ", image.getId()));
                } catch (Exception e) {
                    LOG.warn(e.getMessage());
                }
            }
        }
    }
}