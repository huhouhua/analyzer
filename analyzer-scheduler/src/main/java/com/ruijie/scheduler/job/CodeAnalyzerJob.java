package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Container;
import com.ruijie.core.docker.DockerClientWrapper;
import com.ruijie.scheduler.config.JobConfig;
import com.ruijie.scheduler.config.SchedulerConfig;
import com.ruijie.scheduler.config.SchedulerDockerConfig;
import com.ruijie.scheduler.config.SonarConfigProvider;
import com.ruijie.scheduler.model.Global;
import com.ruijie.scheduler.model.Group;
import com.ruijie.scheduler.model.ProjectConfig;
import com.ruijie.scheduler.model.TaskConfig;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CodeAnalyzerJob implements Job {
    private final Logger LOG = LoggerFactory.getLogger(CodeAnalyzerJob.class.getName());
    private final DockerClientWrapper dockerClientWrapper;
    private final ObjectMapper mapper;
    private final SchedulerConfig schedulerConfig;
    private final JobConfig jobConfig;
    private final SonarConfigProvider sonarConfigProvider;

    @Autowired
    public CodeAnalyzerJob(SchedulerDockerConfig dockerConfig, SonarConfigProvider sonarConfigProvider,
                           SchedulerConfig schedulerConfig, JobConfig jobConfig, ObjectMapper objectMapper) {
        this.jobConfig = jobConfig;
        this.mapper = objectMapper;
        this.sonarConfigProvider = sonarConfigProvider;
        this.schedulerConfig = schedulerConfig;
        this.dockerClientWrapper = DockerClientWrapper.newWrapper(dockerConfig);
    }

    @Override
    public void execute(JobExecutionContext context) throws RuntimeException {
        LOG.info(StrUtil.format("current time:{}", new Date().toString()));
        String taskStr = (String) context.getJobDetail().getJobDataMap().get(SchedulerManager.TASK_KEY);
        try {
            TaskConfig taskConfig = mapper.readValue(taskStr.getBytes(), TaskConfig.class);
            dockerClientWrapper.printDockerInfo();
            for (Group group : taskConfig.getGroups()) {
                if (group.getProjects() == null || group.getProjects().size() == 0) {
                    continue;
                }
                Integer parallel = (group.getParallel() == null || group.getParallel() == 0)
                        ? taskConfig.getGlobal().getParallel() : group.getParallel();

                if (parallel > schedulerConfig.getMaxParallel()) {
                    parallel = schedulerConfig.getMaxParallel();
                }
                //容器运行数控制
                List<Container> containerList = dockerClientWrapper.getRunningOfContainerByList();
                int residue = Math.abs(containerList.size() - schedulerConfig.getContainerRunningCount());
                if (residue > 0 && residue < parallel) {
                    parallel = residue;
                }
                TaskUtil.collate(group.getProjects(), parallel, parallel, true).forEach(g -> {
                    this.start(g, group.getName(), taskConfig.getGlobal());
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void start(List<ProjectConfig> taskList, String groupName, Global global) {
        int totalTask = taskList.size();
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(totalTask);
        try {
            // 创建完成服务
            CompletionService<AnalyzerTask.TaskInfo> completionService = new ExecutorCompletionService<>(executor);

            for (int i = 0; i < totalTask; i++) {
                // 提交任务到完成服务
                completionService.submit(AnalyzerTask.newTask(i, jobConfig, sonarConfigProvider,
                        schedulerConfig, taskList.get(i), global, dockerClientWrapper));
            }
            // 每批任务等待完成后继续提交下一批任务
            waitForCompletion(completionService, totalTask);
            LOG.info(String.format("%s  %d The  tasks for the current batch have been completed!", groupName, totalTask));
        } catch (RejectedExecutionException | NullPointerException e) {
            LOG.error(e.getMessage());
        } finally {
            // 关闭线程池
            executor.shutdown();
        }
    }

    private void waitForCompletion(CompletionService<AnalyzerTask.TaskInfo> completionService, int batchSize) {
        for (int i = 0; i < batchSize; i++) {
            try {
                // 等待一个任务完成，最大等待时长为 maxWaitTime
                Future<AnalyzerTask.TaskInfo> result = completionService.poll(schedulerConfig.getJobWaitTimeSecond(), TimeUnit.SECONDS);
                if (result != null) {
                    AnalyzerTask.TaskInfo info = result.get();
                    LOG.info("job-id:{} {} completed!", info.getTaskId(), info.getName());
                } else {
                    LOG.warn(StrUtil.format("The wait times out.current max wait time is {} second, the tasks in the current batch are not complete!", schedulerConfig.getJobWaitTimeSecond()));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }
    }
}
