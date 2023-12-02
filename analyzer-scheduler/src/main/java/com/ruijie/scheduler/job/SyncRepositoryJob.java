package com.ruijie.scheduler.job;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.core.git.GitProjectConfigProvider;
import com.ruijie.core.git.GitProjectFactory;
import com.ruijie.core.git.GitProjectSupport;
import com.ruijie.core.git.SshCredentialsConfig;
import com.ruijie.scheduler.config.RepositoryConfigProvider;
import com.ruijie.scheduler.model.GitRepository;
import com.ruijie.scheduler.model.RepositoryConfig;
import com.ruijie.scheduler.model.TaskConfig;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.FetchResult;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class SyncRepositoryJob implements Job {
    private final Logger LOG = LoggerFactory.getLogger(SyncRepositoryJob.class.getName());
    private final SchedulerManager container;
    private final ObjectMapper objectMapper;
    private final RepositoryConfig repositoryConfig;
    private final GitRepository gitRepository;
    private final GitProjectSupport gitProjectSupport;
    private static String projectPath;
    private  static  ObjectId currentObjectId;
    private static String currentBranch;

    @Autowired
    public SyncRepositoryJob(ObjectMapper objectMapper,
                             SchedulerManager container,
                             RepositoryConfigProvider provider) {
        this.container = container;
        this.objectMapper = objectMapper;
        this.repositoryConfig = provider.getConfig();
        this.gitRepository = repositoryConfig.getRepository();
        currentBranch = gitRepository.getBranch();
        this.gitProjectSupport = this.createGitSupportInstance();
    }

    @Override
    public void execute(JobExecutionContext context) {
        try {
            boolean exist = FileUtil.exist(projectPath);
            if (!exist) {
                LOG.info("******************* start clone *******************");
                LOG.info(StrUtil.format("clone to {} ...", projectPath));
                this.cloneRepo();
                this.updateTaskConfig();
            } else {
                if (currentBranch != null && !currentBranch.equals(gitRepository.getBranch())) {
                    this.checkout();
                    this.updateTaskConfig();
                } else {
                    ObjectId remoteId = this.getRemoteId();
                    if (!remoteId.equals(currentObjectId)) {
                        this.pull();
                        this.updateTaskConfig();
                    } else {
                        LOG.info(String.format("%s skip pull", currentBranch));
                    }
                }
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private  void  updateTaskConfig() throws JsonProcessingException, SchedulerException {
        TaskConfig taskConfig = this.readTask();
        if (!taskConfig.getGlobal().getTriggerTimeCron().isEmpty()) {
            container.set(taskConfig);
        }
    }

    private ObjectId getRemoteId() throws Exception {
        FetchResult fetchResult = gitProjectSupport.fetch();
        Ref ref = fetchResult.getAdvertisedRef(String.format("refs/heads/%s", gitRepository.getBranch()));
        return ref.getObjectId();
    }

    private void pull() throws Exception {
        PullResult pullResult = gitProjectSupport.pull();
        if (!pullResult.isSuccessful()) {
            LOG.error("pull error!");
        }
        currentObjectId = pullResult.getMergeResult().getNewHead();
    }

    private void cloneRepo() throws Exception {
        Git git = gitProjectSupport.cloneRepo();
        currentBranch = git.getRepository().getBranch();
        currentObjectId = git.getRepository().findRef("HEAD").getObjectId();
    }

    private void checkout() throws Exception {
        gitProjectSupport.fetch();
        Ref ref = gitProjectSupport.checkout();
        currentBranch = ref.getName();
        currentObjectId = ref.getObjectId();
    }

    private TaskConfig readTask() throws JsonProcessingException {
        String path = StrUtil.format("{}{}", projectPath, gitRepository.getTaskFilePath());
        if (!FileUtil.exist(path)) {
            LOG.warn(String.format("%s project file task not exist!", path));
        }
        String content = FileUtil.readUtf8String(path);
        return objectMapper.readValue(content, TaskConfig.class);
    }

    private GitProjectSupport createGitSupportInstance() {
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
