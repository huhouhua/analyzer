package com.ruijie.scheduler.job;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.scheduler.config.RepositoryConfigProvider;
import com.ruijie.scheduler.model.GitRepository;
import com.ruijie.scheduler.model.RepositoryConfig;
import com.ruijie.scheduler.model.TaskConfig;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.quartz.*;
import org.ruijie.core.git.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class SyncRepositoryJob  implements  Job{
    private  final Logger LOG =  LoggerFactory.getLogger(SyncRepositoryJob.class.getName());
    private  final SchedulerManager container;
    private  final ObjectMapper objectMapper;
    private  final RepositoryConfig repositoryConfig;
    private  final GitRepository gitRepository;
    private  final GitProjectSupport gitProjectSupport;
    private   String  projectPath;
    @Autowired
    public SyncRepositoryJob(ObjectMapper objectMapper,
                             SchedulerManager container,
                             RepositoryConfigProvider provider){
        this.container = container;
        this.objectMapper = objectMapper;
        this.repositoryConfig = provider.getConfig();
        this.gitRepository = repositoryConfig.getRepository();
        this.gitProjectSupport = this.createGitSupportInstance();
    }
    @Override
    public void execute(JobExecutionContext context) {
     try {
         boolean exist = FileUtil.exist(projectPath);
         if (!exist){
             LOG.info("******************* start clone *******************");
             LOG.info(StrUtil.format("clone to {}..............",projectPath));
             gitProjectSupport.gitClone();
         }else{
             PullResult result = gitProjectSupport.pull();
             if (!result.isSuccessful()){
                 LOG.error("pull error!");
             }
         }
         TaskConfig taskConfig = this.readTask();
         if (!taskConfig.getGlobal().getTriggerTimeCron().isEmpty()){
             container.set(taskConfig);
         }
     }catch (JsonProcessingException e){
         e.printStackTrace();
         LOG.error(e.getMessage());
     }
     catch (Exception e) {
         throw new RuntimeException(e);
     }
    }
    private TaskConfig  readTask() throws JsonProcessingException {
        String path = StrUtil.format("{}{}",projectPath,gitRepository.getTaskFilePath());
        if (!FileUtil.exist(path)){
             LOG.warn(String.format("%s project file task not exist!",path));
        }
        String content = FileUtil.readUtf8String(path);
       return  objectMapper.readValue(content,TaskConfig.class);
    }
    private  GitProjectSupport createGitSupportInstance(){
        projectPath = StrUtil.format("{}{}",
                repositoryConfig.getStoreDirectory(),
                FileUtil.mainName(gitRepository.getUrl()));
        return GitProjectFactory.createFeatureInstance(
                new GitProjectConfigProvider(gitRepository.getUrl(),
                        projectPath,
                        gitRepository.getBranch(),
                        new SshCredentialsConfig(RepositoryConfigProvider.getSshSessionFactory())));
    }
}
