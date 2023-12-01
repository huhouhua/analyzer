package com.ruijie.scheduler.job;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.core.docker.DockerClientWrapper;
import com.ruijie.scheduler.config.JobConfig;
import com.ruijie.scheduler.config.SchedulerConfig;
import com.ruijie.scheduler.config.SchedulerDockerConfig;
import com.ruijie.scheduler.config.SonarConfigProvider;
import com.ruijie.scheduler.model.Global;
import com.ruijie.scheduler.model.Group;
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

                Integer parallel = (group.getParallel() == null || group.getParallel() == 0)
                        ? taskConfig.getGlobal().getParallel() : group.getParallel();

                if (parallel > schedulerConfig.getMaxParallel()) {
                    parallel = schedulerConfig.getMaxParallel();
                }
                this.start(group, taskConfig.getGlobal(), parallel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void start(Group group, Global global, Integer batchSize) {
        int totalTasks = group.getProjects().size();
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(batchSize);
        // 创建完成服务
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < totalTasks; i++) {
            // 提交任务到完成服务
            completionService.submit(AnalyzerTask.newTask(i, jobConfig.toImage(), sonarConfigProvider, group, global, dockerClientWrapper));
            // 每批任务等待完成后继续提交下一批任务
            if ((i + 1) % batchSize == 0) {
                waitForCompletion(completionService, batchSize);
                LOG.info("All tasks completed. Exiting the loop.");
            }
        }
        // 关闭线程池
        executor.shutdown();
    }

    private void waitForCompletion(CompletionService<Integer> completionService, int batchSize) {
        for (int i = 0; i < batchSize; i++) {
            try {
                // 等待一个任务完成，最大等待时长为 maxWaitTime
                Future<Integer> result = completionService.poll(schedulerConfig.getMaxJobWaitTimeSecond(), TimeUnit.SECONDS);
                if (result != null) {
                    LOG.info("job-id:{} completed!", result.get());
                } else {
                    LOG.warn(StrUtil.format("The wait times out.current max wait time is {} second, the tasks in the current batch are not complete!", schedulerConfig.getMaxJobWaitTimeSecond()));
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
                break;
            }
        }
        LOG.info("The tasks for the current batch have been completed");
    }
}
