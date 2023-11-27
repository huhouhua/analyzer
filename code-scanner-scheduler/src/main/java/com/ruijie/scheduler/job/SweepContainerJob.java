package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.ruijie.scheduler.config.SchedulerDockerConfig;
import com.ruijie.scheduler.config.SweepConfig;
import com.ruijie.scheduler.config.SweepContainerConfig;
import com.ruijie.scheduler.config.SweepImageConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.ruijie.core.docker.DockerClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SweepContainerJob implements Job {
    private final Logger LOG = LoggerFactory.getLogger(SweepImageJob.class.getName());
    private final DockerClientWrapper dockerClientWrapper;
    private final SweepContainerConfig sweepConfig;

    @Autowired
    public SweepContainerJob(SchedulerDockerConfig dockerConfig,
                             SweepConfig sweepConfig) {
        this.sweepConfig = sweepConfig.getContainer();
        this.dockerClientWrapper = DockerClientWrapper.newWrapper(dockerConfig);
    }

    @Override
    public void execute(JobExecutionContext context) {
        List<Container> containerList = dockerClientWrapper.getExitedOfContainerByList();
        for (Container container : containerList) {
            if (sweepConfig.match(container.getCommand())) {
                try {
                    dockerClientWrapper.removeContainer(container.getId(),false);
                    LOG.info(StrUtil.format("remove container {} ", container.getId()));
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }
}
