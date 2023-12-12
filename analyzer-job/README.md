## analyzer-job是什么？
 工作组件，用来扫描项目的，由analyzer-scheduler负责管理此组件，analyzer-scheduler通过启动analyzer-job容器的形式，通过环境变量，把项目、扫描任务等基础信息，传递给此组件，然后开始工作，工作完，会做一些镜像清理工作、以及扫描结果通知等。

## 具体做的事情
 1. 克隆扫描仓库
 2. 解析仓库配置
 3. 注入SonarDockerfile文件内容，${SONAR_ARGS}的值。
 4. 构建SonarDockerfile
 5. 飞书通知

## 如何开发
### 1. 使用IDEA工具，打开此项目。
### 2. 修改环境，打开resources目录下的application.yaml文件，将active设置为dev，那么会以application-dev.yaml为配置文件。
``` yaml
spring:
  profiles:
    active: dev #指定当前环境，目前为dev、prod环境
```
### 3. application-dev.yaml文件，需要修改的参数
``` yaml
git:
  privateKeyFilePath: "I:\\tool\\code-analyzer\\private_key" #这个是克隆仓库用的私钥，如果git地址调整的话，那么需要修改此文件内容，默认是http://172.17.189.70/私钥， 路径修改为当前analyzer-analyzer模块下的private_key文件的绝对路径。
store:
  directory: "I:\\test\\project\\appserver2\\"  #这个是克隆仓库后，存放的目录地址！需要修改。
docker:
  host: tcp://172.17.163.197:2375  #这个是docker服务端的API地址，需要修改。
  apiVersion: 1.41
notification:
  webhook: "https://open.feishu.cn/open-apis/bot/v2/hook/ed495d44-4048-4bb9-afd8-233235b53437"  #扫描结果通知地址，这是飞书群里面机器人的地址，需要修改。
default:
  project: 
    name: appserver  #扫描的项目名称，需要修改
    url: "git@172.17.189.70:riil-insight/riil-insight-appserver.git" #扫描的仓库git地址，需要修改
    branch: "develop"  #分支
    sonarFilePath: "release\\docker\\SonarDockerfile"  #SonarDockerfile文件目录，需要修改
    mode: "Dockerfile"  
    description: "riil-insight-appserver" #项目描述,需要修改
  sonar:
    enable: true  #是否开启sonar为固定，如果true的话，那么构建SonarDockerfile会把结果上传到此服务端上。
    url: "http://172.17.206.162:30090"
    login: admin
    password: "3ed$RF5tg"
```

### 如何部署
``` shell
  cd analyzer-job
  docker build --pull --no-cache -t 172.17.162.231/devops/analyzer-job:版本号 -f  JobDockerfile . 
  docker push 172.17.162.231/devops/analyzer-job:版本号
```
### 如何快速测试
docker run -ti  --name appserver -e JOB_PROJECT_NAME=test -e JOB_PROJECT_URL=git@172.17.189.70:riil-insight/riil-insight-appserver.git  -e JOB_PROJECT_BRANCH=develop  -e JOB_PROJECT_FILE_URL=release/docker/SonarDockerfile -e JOB_PROJECT_MODE=dockerfile -e JOB_PROJECT_DESCRIPTION=test    -e SONAR_ENABLE=true  -e SONAR_URL=http://172.17.206.162:30090   -e SONAR_LOGIN=admin -e SONAR_PASSWORD=3ed$RF5tg  172.17.162.231/devops/scanner-job:1.0

