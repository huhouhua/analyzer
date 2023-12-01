package com.ruijie.core.docker;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.ruijie.core.SonarConfig;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class DockerClientWrapper {
    private final Logger LOG = LoggerFactory.getLogger(DockerClientWrapper.class.getName());
    private final DockerClient dockerClient;
    private static final int MAX_CONNECTIONS = 200;
    private static final int CONNECTION_TIMEOUT_HOURS = 5;
    private static final int RESPONSE_TIMEOUT_HOURS = 5;
    private  static  final  String RUNNING_TAG= "running";
    private  static  final  String EXITED_TAG= "exited";
    public  static  final List<String> allStatus = Arrays.asList(RUNNING_TAG,"created","removing","paused",EXITED_TAG,"dead");
    public  static  final List<String> runningStatus = Collections.singletonList(RUNNING_TAG);

    public  static  final List<String> exitedStatus = Collections.singletonList(EXITED_TAG);

    private  static  final  String  DOCKER_SOCK_PATH="/var/run/docker.sock";
    private  static  final  String DOCKER_FILE_REPLAC_PATTERNSTRING ="\\$\\{SONAR_ARGS\\}";
    private final DockerClientConfig config;

     public DockerClientWrapper(@NonNull DockerClientConfig config) {
        this.config = config;
        this.dockerClient = this.creatClient(config.getHost());
    }
    public static DockerClientWrapper newWrapper(@NonNull DockerClientConfig config){
            return  new DockerClientWrapper(config);
    }
    public CreateContainerResponse createContainers(@NonNull ContainerArgsBuilder containerBuilder) {
         LOG.info(containerBuilder.toString());
        HostConfig hostConfig = new HostConfig().
                withBinds(new Bind(DOCKER_SOCK_PATH, new Volume(DOCKER_SOCK_PATH)));
        return dockerClient.
                createContainerCmd(containerBuilder.getImageName()).
                withName(containerBuilder.getContainerName()).
                withHostConfig(hostConfig).
                withEnv(Helper.ofVariableFormMap(containerBuilder.getEnvsMap())).
                exec();
    }
    public Container getRunningOfContainerById(String containerId) {
        List<String> idList = new ArrayList<>();
        idList.add(containerId);
        List<Container> containerList = dockerClient.listContainersCmd().
                withIdFilter(idList).
                withStatusFilter(runningStatus).
                exec();
        if (containerList.size() > 0) {
            return containerList.get(0);
        }
        return null;
    }
    public Container getContainerByName(String name) {
        List<String> nameList = new ArrayList<>();
        nameList.add(name);
        List<Container> containerList = dockerClient.listContainersCmd().
               withStatusFilter(allStatus).
                withNameFilter(nameList).
                exec();
        Optional<Container> newContainer = containerList.stream().filter(container ->
                Arrays.asList(container.getNames()).contains(StrUtil.format("/{}",name))).findFirst();
        return newContainer.orElse(null);
    }

    public  List<Container> getExitedOfContainerByList(){
      return dockerClient.listContainersCmd().
                withStatusFilter(exitedStatus).
                exec();
    }
    public  List<Container> getRunningOfContainerByList(){
        return dockerClient.listContainersCmd().
                withStatusFilter(runningStatus).
                exec();
    }

    public void startContainer(String containerId) {
        dockerClient.
                startContainerCmd(containerId).
                exec();
    }
    public void removeContainer(String containerId,boolean isForce) {
        dockerClient.
                removeContainerCmd(containerId).
                withForce(isForce).
                exec();

    }
    public String buildImage(String tag, File baseDirectory, File dockerfile) {
        return dockerClient.buildImageCmd().
                withBaseDirectory(baseDirectory).
                withDockerfile(dockerfile).
                withTags(Collections.singleton(tag)).
                withPull(true).
                withNoCache(true).
                exec(new SubscribeBuildImage()).
                awaitImageId();
    }
    public List<Image> getImageList() {
      return  dockerClient.listImagesCmd().
                exec();
    }

    public void removeImage(String imageName,boolean isForce) {
        dockerClient.removeImageCmd(imageName).
                withForce(isForce).
                exec();
    }

    public String buildImageFromSonar(String tag, SonarConfig sonarConfig, File dockerfile) throws IOException {
        // 获取工作目录的绝对路径
        String workspaceDirectory = Paths.get(dockerfile.getAbsolutePath(), "../../..").
                normalize().
                toAbsolutePath().
                toString();
         LOG.info(sonarConfig.toString());
         String sonarArgs =  DockerSonarArgsBuilder.newBuilder(sonarConfig).buildOfAll();
         LOG.info(sonarArgs);
        Helper.replacTextContent(dockerfile, DOCKER_FILE_REPLAC_PATTERNSTRING, sonarArgs);
        return buildImage(tag,new File(workspaceDirectory), dockerfile);
    }

    private DockerClient creatClient(String host) {
        // 配置docker CLI的一些选项
        DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost(host).
                withDockerTlsVerify(false)
                // 与docker版本对应，参考https://docs.docker.com/engine/api/#api-version-matrix
                // 或者通过docker version指令查看api version
                .build();

        // 创建DockerHttpClient
        DockerHttpClient httpClient = new ApacheDockerHttpClient
                .Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(MAX_CONNECTIONS)
                .connectionTimeout(Duration.ofHours(CONNECTION_TIMEOUT_HOURS))
                .responseTimeout(Duration.ofHours(RESPONSE_TIMEOUT_HOURS))
                .build();
        return DockerClientImpl.getInstance(config, httpClient);
    }

    public void printDockerInfo() {
         LOG.info("printDockerInfo.....");
         Info info = dockerClient.infoCmd().exec();
        LOG.info("exec infoCmd end");
        String infoStr = JSONUtil.toJsonPrettyStr(info);
        LOG.info("docker info ");
        LOG.info(infoStr);

    }
}
