## code-scheduler是什么？
  调度组件，用来管理job，主要负责定时触发扫描任务，以及同步存储扫描仓库任务，另外还负责镜像、容器清理工作。
## 具体做的事情
1. 同步存储扫描仓库任务
2. 定时触发扫描任务
3. 清理扫描垃圾容器
4. 清理扫描垃圾镜像

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
git-repository:
  syncTimeCron: "0/30 * * * * ?"  #间隔多久同步， cron表达式。
  storeDirectory: "D:/demo/job/" #存储扫描任务定义仓库存放目录，本地地址，需要修改
  repository:
    url: "git@172.17.189.70:wangyj/riil-sonar.git"  #仓库扫描任务定义git地址，需要修改
    branch: "dev"  #仓库任务扫描的分支  需要修改
    taskFilePath: "/tasks/test.yaml" #扫描的任务文件，以yaml格式，需要修改
    privateKeyFilePath: "I:\\tool\\code-analyzer\\analyzer-scheduler\\private_key"  #这个是克隆仓库用的私钥，如果git地址调整的话，那么需要修改此文件内容，默认是http://172.17.189.70/私钥， 路径修改为当前analyzer-analyzer模块下的private_key文件的绝对路径。
docker:
  host: tcp://172.17.163.197:2375  #这个是docker服务端的API地址，需要修改。
  apiVersion: 1.41
scheduler:
  maxParallel: 10 #job 最大并行数
  containerRunningCount: 20 #容器运行数
  jobWaitTimeSecond: 7200 # 运行job 2小时的等待时长，单位为秒
job:
  tag: 1.0  #analyzer-job组件的镜像版本，需要修改
  repository: 172.17.162.231/devops/analyzer-job  #analyzer-job组件的镜像地址，需要修改
  notifyWebhook: "https://open.feishu.cn/open-apis/bot/v2/hook/ed495d44-4048-4bb9-afd8-233235b53437"
sweep:
  matchExpressions:
    operator: NotIn
    values:
      - ".*scanner-job.*"
  container:
    timeCron: "0/5 * * * * ?"
    timeoutHours: 1
    matches:
      - ".*scanner-job.*"
      - ".*sonar.*"
  image:
    timeCron: "0/5 * * * * ?"
    matches:
      - ".*<none>.*"
sonar:
  enable: true #是否开启sonar为固定，如果true的话，那么构建SonarDockerfile会把结果上传到此服务端上。
  url: "http://172.17.206.162:30090" #sonar服务端地址， 需要修改
  login: "admin"  #sonar登录账号，需要修改
  password: "3ed$RF5tg"  #sonar登录密码，需要修改
```
### 如何部署
``` shell
  cd code-analyzer
  docker build --pull --no-cache -t 172.17.162.231/devops/analyzer-scheduler:版本号 -f  SchedulerDockerfile .
  docker push 172.17.162.231/devops/analyzer-scheduler:版本号
```
