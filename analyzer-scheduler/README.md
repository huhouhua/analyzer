## code-scheduler
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

### 如何部署
``` shell
  cd analyzer-job
  docker build -t 172.17.162.231/devops/analyzer-job:版本号 .
  docker push 172.17.162.231/devops/analyzer-job:版本号
```


