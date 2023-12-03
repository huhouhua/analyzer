package com.ruijie.scheduler.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.Container;
import com.ruijie.core.ProjectSonarConfigContract;
import com.ruijie.core.docker.ContainerArgsBuilder;
import com.ruijie.core.docker.DockerClientWrapper;
import com.ruijie.scheduler.config.JobConfig;
import com.ruijie.scheduler.config.SchedulerConfig;
import com.ruijie.scheduler.config.SonarConfigProvider;
import com.ruijie.scheduler.model.Global;
import com.ruijie.scheduler.model.Group;
import com.ruijie.scheduler.model.ProjectConfig;
import com.ruijie.scheduler.model.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

public class AnalyzerTask implements Callable<AnalyzerTask.TaskInfo> {
    private final Logger LOG = LoggerFactory.getLogger(AnalyzerTask.class.getName());
    private final Integer QUERY_STATE_DELAY_SECOND = 30 * 1000;  //30秒
    private final Integer RUNNING_DELAY_SECOND = 5 * 1000;  //5秒
    private final DockerClientWrapper dockerClientWrapper;
    private final int taskId;
    private final JobConfig jobConfig;
    private final ProjectConfig project;
    private final SchedulerConfig schedulerConfig;
    private final SonarConfigProvider sonarConfigProvider;
    private final Global global;

    public AnalyzerTask(int taskId,
                        JobConfig jobConfig,
                        SonarConfigProvider sonarConfigProvider,
                        SchedulerConfig schedulerConfig,
                        ProjectConfig project,
                        Global global,
                        DockerClientWrapper dockerClientWrapper) {
        this.taskId = taskId;
        this.jobConfig = jobConfig;
        this.project = project;
        this.global = global;
        this.schedulerConfig = schedulerConfig;
        this.dockerClientWrapper = dockerClientWrapper;
        this.sonarConfigProvider = sonarConfigProvider;
    }

    public static Callable<AnalyzerTask.TaskInfo> newTask(int taskId,
                                                          JobConfig jobConfig,
                                                          SonarConfigProvider sonarConfigProvider,
                                                          SchedulerConfig schedulerConfig,
                                                          ProjectConfig project,
                                                          Global global,
                                                          DockerClientWrapper dockerClientWrapper) {
        return new AnalyzerTask(taskId, jobConfig, sonarConfigProvider, schedulerConfig, project, global, dockerClientWrapper);
    }

    @Override
    public AnalyzerTask.TaskInfo call() {
        ContainerArgsBuilder builder = this.containerArgsBuilder();
        CreateContainerResponse response = this.startContainer(builder);
        if (response != null) {
            this.waitRunToComplete(response.getId(), builder.getContainerName());
        }
        return TaskInfo.newInfo(project.getName(), taskId);
    }
    private CreateContainerResponse startContainer(ContainerArgsBuilder builder) {
        //todo 容器数控制为10个,再启动当前容器
        CreateContainerResponse response = null;
        while (true) {
            try {
                List<Container> containerList = dockerClientWrapper.getRunningOfContainerByList();
                if (containerList.size() >= schedulerConfig.getContainerRunningCount()) {
                    LOG.info(StrUtil.format("job-id:{} current running container count {}, {} sleep {} second running...........", taskId,
                            containerList.size(), builder.getContainerName(), RUNNING_DELAY_SECOND));
                    Thread.sleep(RUNNING_DELAY_SECOND);
                    continue;
                }
                LOG.info(StrUtil.format("job-id:{} {} run {} image", taskId, builder.getContainerName(), builder.getImageName()));
                response = dockerClientWrapper.createContainers(builder);
                LOG.info(StrUtil.format("job-id:{} {} run {} container", taskId, builder.getContainerName(), response.getId()));
                dockerClientWrapper.startContainer(response.getId());
                break;
            } catch (NotFoundException | NotModifiedException e) {
                LOG.warn(e.getMessage());
                break;
            } catch (InterruptedException e) {
                LOG.warn(e.getMessage());
            }
        }
        return response;
    }

    private void waitRunToComplete(String containerId, String containerName) {
        while (true) {
            try {
                LOG.info(StrUtil.format("job-id:{} {} query {} container", taskId, containerName, containerId));
                Container container = dockerClientWrapper.getRunningOfContainerById(containerId);
                if (container == null) {
                    dockerClientWrapper.removeContainer(containerId, true);
                    break;
                }
                LOG.info(StrUtil.format("job-id:{} {} container status is {}", taskId, containerName, container.getStatus()));
                LOG.info(StrUtil.format("job-id:{} {} sleep {} second...........", taskId, containerName, QUERY_STATE_DELAY_SECOND));
                Thread.sleep(QUERY_STATE_DELAY_SECOND);
            } catch (NotFoundException e) {
                LOG.error(StrUtil.format("job-id:{} container:{} err:{}", taskId, containerName, e.getMessage()));
                break;
            } catch (InterruptedException e) {
                LOG.warn(e.getMessage());
            }
        }
    }

    private ContainerArgsBuilder containerArgsBuilder() {
        ContainerArgsBuilder builder = ContainerArgsBuilder.newBuilder(jobConfig.toImage()).
                withEnvsMap(generateVariable(global)).
                withContainerName(project.getName());
        if (dockerClientWrapper.getContainerByName(project.getName()) != null) {
            builder.withContainerName(StrUtil.format("{}-{}-{}", project.getName(), DateUtil.current(), taskId));
        }
        return builder;
    }

    private Map<String, String> generateVariable(Global global) {
        Map<String, String> map = new HashMap<>();
        ProjectRepository repository = project.getRepo();
        map.put(ProjectSonarConfigContract.PROJECT_NAME, project.getName());
        map.put(ProjectSonarConfigContract.PROJECT_URL, repository.getUrl());
        map.put(ProjectSonarConfigContract.PROJECT_BRANCH_TAG, trySetDefault(repository.getBranch(), global.getRepo().getBranch()));
        map.put(ProjectSonarConfigContract.PROJECT_SONAR_FILE_URL_TAG, trySetDefault(repository.getSonarFileUrl(), global.getSonar().getDockerFileUrl()));
        map.put(ProjectSonarConfigContract.PROJECT_SONAR_MODE_TAG, trySetDefault(project.getMode(), global.getSonar().getMode()));
        map.put(ProjectSonarConfigContract.PROJECT_DESCRIPTION_TAG, project.getDescription());
        map.put(ProjectSonarConfigContract.NOTIFY_WEBHOOK_TAG, jobConfig.getNotifyWebhook());
        this.setVariableForSonar(map);
        return map;
    }

    private void setVariableForSonar(Map<String, String> map) {
        map.put(ProjectSonarConfigContract.SONAR_ENABLE_TAG, String.valueOf(sonarConfigProvider.getEnable()));
        map.put(ProjectSonarConfigContract.SONAR_URL_TAG, sonarConfigProvider.getUrl());
        map.put(ProjectSonarConfigContract.SONAR_LOGIN_TAG, sonarConfigProvider.getLogin());
        map.put(ProjectSonarConfigContract.SONAR_PASSWORD_TAG, sonarConfigProvider.getPassword());
    }

    private String trySetDefault(String value, String defaultValue) {
        return (value == null || Objects.equals(value, "")) ?
                defaultValue : value;
    }

    public static class TaskInfo {
        private final String name;
        private final Integer taskId;

        public TaskInfo(String name, Integer taskId) {
            this.name = name;
            this.taskId = taskId;
        }

        public static TaskInfo newInfo(String name, Integer taskId) {
            return new TaskInfo(name, taskId);
        }

        public String getName() {
            return name;
        }

        public Integer getTaskId() {
            return taskId;
        }

    }
}

