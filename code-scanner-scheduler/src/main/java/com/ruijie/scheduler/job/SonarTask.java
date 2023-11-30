package com.ruijie.scheduler.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.ruijie.scheduler.config.SonarConfigProvider;
import com.ruijie.scheduler.model.Global;
import com.ruijie.scheduler.model.ProjectConfig;
import com.ruijie.scheduler.model.Group;
import com.ruijie.scheduler.model.ProjectRepository;
import org.ruijie.core.ProjectSonarConfigContract;
import org.ruijie.core.docker.ContainerArgsBuilder;
import org.ruijie.core.docker.DockerClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

public class SonarTask implements Callable<Integer> {
    private final Logger LOG = LoggerFactory.getLogger(SonarTask.class.getName());
    private final Integer QUERY_STATE_DELAY_SECOND = 30 * 1000;  //30秒
    private final DockerClientWrapper dockerClientWrapper;
    private final int taskId;
    private final String image;
    private final Group group;
    private  final SonarConfigProvider sonarConfigProvider;

    private final Global global;
    public SonarTask(int taskId,
                     String image,
                     SonarConfigProvider sonarConfigProvider,
                     Group group,
                     Global global,
                     DockerClientWrapper dockerClientWrapper) {
        this.taskId = taskId;
        this.image = image;
        this.group = group;
        this.global = global;
        this.dockerClientWrapper = dockerClientWrapper;
        this.sonarConfigProvider = sonarConfigProvider;
    }

    public static Callable<Integer> newTask(int taskId,
                                            String image,
                                            SonarConfigProvider sonarConfigProvider,
                                            Group group,
                                            Global global,
                                            DockerClientWrapper dockerClientWrapper) {
        return new SonarTask(taskId, image, sonarConfigProvider, group, global, dockerClientWrapper);
    }

    @Override
    public Integer call() {
        ProjectConfig group = this.getGroup();
        ContainerArgsBuilder builder = this.containerArgsBuilder(group);
        CreateContainerResponse response = this.startContainer(builder);
        this.waitRunToComplete(response.getId(), builder.getContainerName());
        return taskId;
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
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOG.error(StrUtil.format("job-id:{} container:{} err:{}", taskId, containerName, e.getMessage()));
                break;
            }
        }
    }

    private CreateContainerResponse startContainer(ContainerArgsBuilder builder) {
        LOG.info(StrUtil.format("job-id:{} {} run {} image", taskId, builder.getContainerName(), builder.getImageName()));
        CreateContainerResponse response = dockerClientWrapper.createContainers(builder);
        LOG.info(StrUtil.format("job-id:{} {} run {} container", taskId, builder.getContainerName(), response.getId()));
        dockerClientWrapper.startContainer(response.getId());
        return response;
    }

    private ContainerArgsBuilder containerArgsBuilder(ProjectConfig group) {
        ContainerArgsBuilder builder = ContainerArgsBuilder.newBuilder(image).
                withEnvsMap(generateVariable(group, global)).
                withContainerName(group.getName());
        if (dockerClientWrapper.getContainerByName(group.getName()) != null) {
            builder.withContainerName(StrUtil.format("{}-{}-{}", group.getName(), DateUtil.current(), taskId));
        }
        return builder;
    }

    private ProjectConfig getGroup() {
        return group.getProjects().get(taskId);
    }

    private Map<String, String> generateVariable(ProjectConfig group, Global global) {
        Map<String, String> map = new HashMap<>();
        ProjectRepository repository = group.getRepo();
        map.put(ProjectSonarConfigContract.PROJECT_NAME, group.getName());
        map.put(ProjectSonarConfigContract.PROJECT_URL, repository.getUrl());
        map.put(ProjectSonarConfigContract.PROJECT_BRANCH_TAG, trySetDefault(repository.getBranch(), global.getRepo().getBranch()));
        map.put(ProjectSonarConfigContract.PROJECT_SONAR_FILE_URL_TAG, trySetDefault(repository.getSonarFileUrl(), global.getSonar().getDockerFileUrl()));
        map.put(ProjectSonarConfigContract.PROJECT_SONAR_MODE_TAG, trySetDefault(group.getMode(), "dockerfile"));
        map.put(ProjectSonarConfigContract.PROJECT_DESCRIPTION_TAG, group.getDescription());
        this.setVariableForSonar(map);
        return map;
    }

    private  void  setVariableForSonar(Map<String, String>  map){
        map.put(ProjectSonarConfigContract.SONAR_ENABLE_TAG, String.valueOf(sonarConfigProvider.getEnable()));
        map.put(ProjectSonarConfigContract.SONAR_URL_TAG, sonarConfigProvider.getUrl());
        map.put(ProjectSonarConfigContract.SONAR_LOGIN_TAG, sonarConfigProvider.getLogin());
        map.put(ProjectSonarConfigContract.SONAR_PASSWORD_TAG, sonarConfigProvider.getPassword());
    }

    private String trySetDefault(String value, String defaultValue) {
        return (value == null || Objects.equals(value, "")) ?
                defaultValue : value;
    }
}
