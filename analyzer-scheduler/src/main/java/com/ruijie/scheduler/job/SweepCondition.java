package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.model.Container;
import com.ruijie.core.docker.DockerClientWrapper;
import com.ruijie.scheduler.config.MatchExpressionsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class SweepCondition {
    private final Logger LOG = LoggerFactory.getLogger(SweepCondition.class.getName());
    private final DockerClientWrapper dockerClientWrapper;
    private  final MatchExpressionsConfig matchExpressionsConfig;

    public SweepCondition( DockerClientWrapper dockerClientWrapper,
                           MatchExpressionsConfig matchExpressionsConfig) {
        this.dockerClientWrapper = dockerClientWrapper;
        this.matchExpressionsConfig = matchExpressionsConfig;
    }
    public  boolean match(){
        List<Container> containerList = dockerClientWrapper.getRunningOfContainerByList();
         Optional<Container> matchContainer = containerList.stream().
                 filter(container -> matchExpressionsConfig.match(container.getCommand())).findAny();
        LOG.info(StrUtil.format("current whether the conditions for sweep are available {}",matchContainer.isPresent()));
         return  matchContainer.isPresent();
    }
}
