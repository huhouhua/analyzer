package com.ruijie.job;

import com.ruijie.job.config.GitRepositoryConfig;
import com.ruijie.job.config.ProjectConfigProvider;
import com.ruijie.job.config.RepositoryConfigProvider;
import com.ruijie.job.middleware.CodeScnaProcess;
import com.ruijie.job.middleware.ScanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class StartApplicationListener implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private  final Logger LOG =  LoggerFactory.getLogger(StartApplicationListener.class.getName());
    private transient AutowireCapableBeanFactory beanFactory;

    @Autowired
    public  StartApplicationListener(GitRepositoryConfig gitRepositoryConfig) throws FileNotFoundException {
        this.initialize(gitRepositoryConfig);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            LOG.info("start job");
           CodeScnaProcess.prepare(this.createContext(),beanFactory).run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private ScanContext createContext(){
        return  new ScanContext(
                ProjectConfigProvider.getRepoName(),
                ProjectConfigProvider.getRepoUrl(),
                ProjectConfigProvider.getBranch(),
                ProjectConfigProvider.getSonarFileUrl(),
                ProjectConfigProvider.getMode(),
                ProjectConfigProvider.getDescription()
        );
    }
    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    private void initialize(GitRepositoryConfig  config) throws FileNotFoundException {
        RepositoryConfigProvider.initGitConfig(config);
    }
}
