## code-analyzer
code-analyzer 是基于java 8 spring boot开发的分布式定时代码扫描工具，基于容器调度，以GitOps的思想设计， 使用docker做为运行环境，您可以在 Windows、Linux 或 Mac上安装docker运行它。
## 功能清单
1. 支持dockerfile的方式，扫描项目。
2. 支持扫描在git上托管的项目， 任何第三方git托管平台都支持。
3. 以git存储扫描任务仓库定义为入口，仓库发生任何的变更，立马会同步最新项目扫描任务。
4. 支持横向扩展的功能，如果扫描不够快的话，或者需要支持多项目与多分支的场景，那么以启动多个scheduler容器来调度。
5. 支持cron表达式定义扫描时间。
6. 支持飞书通知，项目扫描完，会把扫描结果以接口的形式通知到飞书群。
### 项目结构介绍
analyzer-core: 基础类库，主要有docker、git的类库封装，通用的配置定义等，提供给analyzer-scheduler、analyzer-job组件使用。

analyzer-job: 工作组件，主要负责扫描项目，一个job扫描一个项目，以容器的形式运行，由analyzer-scheduler组件负责调度他。 

analyzer-scheduler: 调度组件，主要负责同步扫描任务仓库、调度job、以及job资源回收、镜像与容器清理工作。 

## 架构解析
![analyzer.png](http://172.17.162.204/huhouhua/code-scanner/-/raw/master/document/%E6%9E%B6%E6%9E%84%E5%9B%BE.png)
## 如何使用？
### 基本使用
#### 一、环境准备
1. docker安装
 1. 对版本没有要求。
 2. 需要
2. 

#### 二、配置存储扫描任务
1. 打开git托管存储扫描仓库，如果没有的话，请创建仓库！
2. 在任意的目录下，创建以yaml格式的文件，填写以下内容
```yaml
global:
  sonar:
    dockerFilePath: release/docker/SonarDockerfile #dockerfile 扫描文件的默认路径，这个路径是任务仓库里面的路径
    nativeFilePath: sonar-project.properties  #原生扫描文件的路径，目前未实现此方式。
    mode: dockerfile  #默认方式
  parallel: 5 #并行数，每一组group的项目一次扫描多少个项目，如果group没有指定parallel参数的话，那么会默认使用这个并行数。
  triggerTimeCron: "0 */40 * * * ?" #任务扫描时间，也就是什么时候开始扫描，用的是cron表达式，快速生成的表达式，请仓库[cron在线生成](https://www.pppet.net/)
  repo:
    branch: develop  # 扫描的仓库分支，如果projects没有指定branch参数的话，那么会默认使用这个分支。
groups: #组定义，可以定义多个组，放在最前面的组，最先开始扫描。
- name: app #组名，可以重复，可以简单的定义为比如xxx业务下所有的前端服务，用英文表示
  parallel: 4 #并行数，一次扫描，扫描多少个项目。
  description: "应用组" #组描述
  projects: # 项目定义
  - name: appserver #项目名，可以重复
    mode: dockerfile #项目扫描方式
    description: "appserver"  #项目描述
    repo: #仓库定义
      url: git@172.17.189.70:riil-insight/riil-insight-appserver.git #仓库地址，只支持ssh方式。
      branch: develop #仓库扫描的分支
      sonarFilePath: release/docker/SonarDockerfile #dockerfile 扫描的文件路径，这个路径是任务仓库里面的路径。
  - name: ruizhi-cbb #定义第二个项目，其他的配置默认使用全局定义的。
    description: "ruizhi-cbb"
    repo:
      url: git@172.17.189.70:ruizhi-cbb/ruizhi-cbb.git
```
#### 三、运行

### 高级用法

## 如何开发？


