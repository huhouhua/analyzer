## analyzer-job
 工作组件，用来扫描项目的，由analyzer-scheduler负责管理此组件，analyzer-scheduler通过启动analyzer-job容器的形式，通过环境变量，把项目、扫描任务等基础信息，传递给此组件，然后开始工作，工作完，会做一些镜像清理工作、以及扫描结果通知等。

## 具体做的事情
 1. 克隆扫描仓库
 2. 解析仓库配置
 3. 注入SonarDockerfile文件内容，${SONAR_ARGS}的值。
 4. 构建SonarDockerfile
 5. 飞书通知

## 如何开发

## code-analyzer功能清单
1. 支持dockerfile的方式，扫描项目。
2. 支持扫描在git上托管的项目， 任何第三方git托管平台都支持。
3. 以git存储扫描任务仓库定义为入口，仓库发生任何的变更，立马会同步最新项目扫描任务。
4. 支持横向扩展的功能，如果扫描不够快的话，或者需要支持多项目与多分支的场景，那么以启动多个scheduler容器来调度。
5. 支持cron表达式定义扫描时间。
6. 支持飞书通知，项目扫描完，会把扫描结果以接口的形式通知到飞书群。
### 项目结构介绍
analyzer-core: 基础类库，主要有docker、git的类库封装，通用的配置定义等，提供给analyzer-scheduler、analyzer-job组件使用。

analyzer-job: 工作组件，主要负责扫描项目，一个job扫描一个项目，以容器的形式运行，由analyzer-scheduler组件负责调度他。 

analyzer-scheduler: 调度组件，主要负责同步扫描任务仓库、调度与管理job、以及job资源回收、镜像与容器清理工作。 

## 架构解析

## 如何使用？

## 如何开发？


