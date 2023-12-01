package com.ruijie.job;

import com.ruijie.job.config.GitRepositoryConfig;
import com.ruijie.job.config.RepositoryConfigProvider;
import com.ruijie.job.middleware.AnalyzerContext;
import com.ruijie.job.middleware.CodeAnalyzerProcess;
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
           CodeAnalyzerProcess.prepare(this.createContext(),beanFactory).run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private AnalyzerContext createContext(){
        final AnalyzerContext context = beanFactory.createBean(AnalyzerContext.class);
        beanFactory.autowireBean(context);
        return context;
    }
    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    private void initialize(GitRepositoryConfig  config) throws FileNotFoundException {
        RepositoryConfigProvider.initGitConfig(config);
    }
}
