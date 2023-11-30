package com.ruijie.job.middleware;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.ruijie.core.docker.DockerClientConfig;
import org.ruijie.core.Middleware;
import org.ruijie.core.MiddlewareNext;
import org.ruijie.core.docker.DockerClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class CodeSonarMiddleware implements Middleware<SonarContext> {
    private final Logger LOG = LoggerFactory.getLogger(CodeSonarMiddleware.class.getName());
    private  final DockerClientWrapper dockerClientWrapper;
    public CodeSonarMiddleware(
            DockerClientConfig dockerClientConfig){
        LOG.info(dockerClientConfig.toString());
        this.dockerClientWrapper = DockerClientWrapper.newWrapper(dockerClientConfig);
    }

    @Override
    public Object invoke(Object prev, SonarContext ctx, MiddlewareNext next)  throws  Exception {
        LOG.info("code scan .....");
        if (!(prev instanceof String)){
            return  new Exception("sonarFilePath not found!");
        }
        String tag =  StrUtil.format("job:{}", DateUtil.current());
        LOG.info(StrUtil.format("image tag:{}",tag));
        dockerClientWrapper.printDockerInfo();
        String sonarFilePath = prev.toString();
        LOG.info(sonarFilePath);
        LOG.info("--------------------------------start build image --------------------------------------");
        String imageId = dockerClientWrapper.buildImageFromSonar(tag,
                ctx.getSonar(),new File(sonarFilePath));
        if (!Objects.equals(imageId, "")){
            try {
                dockerClientWrapper.removeImage(imageId,true);
            }catch (Exception e){
                LOG.warn(e.getMessage());
                e.printStackTrace();
            }
        }
        LOG.info("code scan succeed！");
        return  "";
    }
}


