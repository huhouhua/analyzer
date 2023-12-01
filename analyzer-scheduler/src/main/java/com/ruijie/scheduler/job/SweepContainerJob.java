package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.model.Container;
import com.ruijie.core.docker.DockerClientWrapper;
import com.ruijie.scheduler.config.SchedulerDockerConfig;
import com.ruijie.scheduler.config.SweepConfig;
import com.ruijie.scheduler.config.SweepContainerConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
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
    private final SweepCondition sweepCondition;

    @Autowired
    public SweepContainerJob(SchedulerDockerConfig dockerConfig,
                             SweepConfig sweepConfig) {
        this.sweepConfig = sweepConfig.getContainer();
        this.dockerClientWrapper = DockerClientWrapper.newWrapper(dockerConfig);
        this.sweepCondition = new SweepCondition(dockerClientWrapper, sweepConfig.getMatchExpressions());
    }

    @Override
    public void execute(JobExecutionContext context) {
        if (sweepCondition.match()) {
            return;
        }
        List<Container> containerList = dockerClientWrapper.getExitedOfContainerByList();
        for (Container container : containerList) {
            if (sweepConfig.match(container.getCommand())) {
                try {
                     boolean isForce =this.hasForce(container.getCreated());
                    dockerClientWrapper.removeContainer(container.getId(),isForce);
                    LOG.info(StrUtil.format("remove container {} force {}", container.getId(),isForce));
                } catch (Exception e) {
                    LOG.warn(e.getMessage());
                }
            }
        }
    }

    private boolean hasForce(long createdTimestamp) {
        long currentTimestamp = System.currentTimeMillis();
        long timeDifference = currentTimestamp - (createdTimestamp * 1000L);
        long hours = timeDifference / (1000 * 60 * 60);
        return hours > sweepConfig.getTimeoutHours();
    }
}
