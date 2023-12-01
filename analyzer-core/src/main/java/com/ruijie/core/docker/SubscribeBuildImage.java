package com.ruijie.core.docker;

import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class SubscribeBuildImage extends BuildImageResultCallback {
    private final Logger LOG = LoggerFactory.getLogger(SubscribeBuildImage.class.getName());
    @Override
    public void onNext(BuildResponseItem item) {
        printError(item);
        printStatus(item);
        printStream(item);
        printProgress(item);
        super.onNext(item);
    }
    private  void  printError(BuildResponseItem response){
        if (!response.isErrorIndicated()){
           return;
        }
        LOG.info(Objects.requireNonNull(response.getErrorDetail()).getMessage());
    }
    private  void printStatus(BuildResponseItem response){
        if (response.getStatus()==null || Objects.equals(response.getStatus(), "\n")){
          return;
        }
        LOG.info(StrUtil.format("{}:{}",response.getStatus(),response.getId()));
    }
    private  void  printStream(BuildResponseItem response){
        if(response.getStream()==null ||  Objects.equals(response.getStream(), "\n")){
         return;
        }
        LOG.info(response.getStream().replace("\n",""));
    }
    private  void  printProgress(BuildResponseItem response){
        Handler consoleHandler = new ConsoleHandler();
        if(response.getProgress()==null ||  Objects.equals(response.getProgress(), "\n")){
          return;
        }
        LOG.info(response.getProgress());
    }

}
