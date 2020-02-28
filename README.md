# dcy-microservices-platform

<p align="center">
  <img src='https://img.shields.io/badge/license-Apache%202-4EB1BA.svg' alt='License'/>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.2.2.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-Hoxton.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.2.0.RELEASE-blue" alt="Downloads"/>
</p>

## 如果您觉得有帮助，请点右上角 "Star" 支持一下谢谢

[TOC]


[又一个开源springcloud项目来了，dcy-microservices-platform](https://blog.csdn.net/qq_33842795/article/details/101436805)<br>
[dcy-microservices-platform-1、项目模块以及目录解释](https://blog.csdn.net/qq_33842795/article/details/101437030)<br>
[dcy-microservices-platform-2、前端项目配置](https://blog.csdn.net/qq_33842795/article/details/101438176)<br>
[dcy-microservices-platform-3、数据库表结构设计](https://blog.csdn.net/qq_33842795/article/details/101438822)<br>
[dcy-microservices-platform-4、前端table-page组件封装和axios封装](https://blog.csdn.net/qq_33842795/article/details/101439207)<br>
[dcy-microservices-platform-5、环境准备以及运行](https://blog.csdn.net/qq_33842795/article/details/101439634)<br>
[安装harbor1.8.2 企业级镜像仓库](https://blog.csdn.net/qq_33842795/article/details/100030657)<br>
[docker 搭建Gitlab以及和jenkins配置](https://blog.csdn.net/qq_33842795/article/details/101448739)<br>
[dcy-microservices-platform-k8s第一步运行环境](https://blog.csdn.net/qq_33842795/article/details/101443722)<br>
[dcy-microservices-platform-k8s-rancher2.x 运行k8s](https://blog.csdn.net/qq_33842795/article/details/101449802)<br>
[dcy-microservices-platform-k8s第二步rancher添加应用商店和nfs-client-provisioner](https://blog.csdn.net/qq_33842795/article/details/101449558)<br>
[dcy-microservices-platform-k8s第二步rancher运行高可用jenkins](https://blog.csdn.net/qq_33842795/article/details/101450213)<br>



## 1.项目介绍
* 前后端分离的企业级微服务架构
* 主要针对解决微服务和业务开发时常见的**非功能性需求**
* 深度定制`Spring Security`真正实现了基于`RBAC`、`jwt`和`oauth2`的无状态统一权限认证的解决方案
* 提供应用管理，方便第三方系统接入
* 引入组件化的思想实现高内聚低耦合并且高度可配置化
* 注重代码规范，严格控制包依赖，每个工程基本都是最小依赖
* 非常适合学习和企业中使用

> 重构于开源项目OCP&cp：https://gitee.com/owenwangwen/open-capacity-platform 
>
> 重构于开源项目zlt：https://gitee.com/zlt2000/microservices-platform

&nbsp;

## 2. 项目总体架构图

<p align="center">
  <img src="/pic/architecture-diagram.png" alt="Downloads"/>
</p>



## 3. 功能介绍
* **统一认证功能**
  * 支持oauth2的四种模式登录
  * 支持用户名、密码加图形验证码登录
  * 支持手机号加密码登录
  * 支持openId登录
  * 支持第三方系统单点登录
* **分布式系统基础支撑**
  - 服务注册发现、路由与负载均衡
  - 服务降级与熔断
  - 服务限流(url/方法级别)
  - 统一配置中心
  - 统一日志中心
  - 统一搜索中心
  - 统一分布式缓存操作类、cacheManager配置扩展
  - 分布式锁
  - 分布式任务调度器
  - 支持CI/CD持续集成(包括前端和后端)
  - 分布式Id生成器
  - 分布式事务(强一致性/最终一致性)
  - 日志链路追踪
* **系统监控功能**
  - 服务调用链监控
  - 应用拓扑图
  - 慢查询SQL监控
  - 应用吞吐量监控(qps、rt)
  - 服务降级、熔断监控
  - 服务限流监控
  - 微服务服务监控
  - 服务器监控
  - redis监控
  - mysql监控
  - elasticSearch监控
  - nacos监控
  - prometheus监控
* **业务基础功能支撑**
  * 多租户(应用隔离)
  * 高性能方法级幂等性支持
  * RBAC权限管理，实现细粒度控制(方法、url级别)
  * 快速实现导入、导出功能
  * 数据库访问层自动实现crud操作
  * 代码生成器
  * 基于Hutool的各种便利开发工具
  * 网关聚合所有服务的Swagger接口文档
  * 统一跨域处理
  * 统一异常处理

&nbsp;

## 4. 模块说明

```lua
dcy-microservices-platform -- 父项目，公共依赖
│  ├─dcy-auth -- auth父级模块
│  │  ├─auth-center-provider -- 认证中心[7777]
│  │─dcy-business -- 父级模块
│  │  ├─admin-center -- 后台管理模块
│  │  │  ├─admin-center-api -- 后台管理模块api
│  │  │  ├─admin-center-provider -- 后台管理模块服务提供者[8999]
│  │  ├─file-center -- 文件模块[5000]
│  │  ├─monitor-center -- 监控模块[8764]
│  │  ├─workflow-center -- 工作流模块[8087]
│  ├─dcy-gateway -- 第二代网关[9001]
│  │─spring-cloud-alibaba-dependencies -- spring boot、cloud、alibab的dependencies
│  │─spring-cloud-alibaba-nacos -- nacos注册中心和配置中心依赖
│  │─spring-cloud-swagger-stater -- swagger接口文档依赖
```

&nbsp;

## 5. 环境准备
nacos、redis、fastdfs

基于docker安装

### nacos 启动


```$xslt
下载项目
git clone https://github.com/nacos-group/nacos-docker.git
cd nacos-docker

单机模式 Mysql
docker-compose -f example/standalone-mysql.yaml up

如果内存不是很充足请删除prometheus和grafana模块，以保证资源
```

### redis启动
```$xslt
redis启动
docker run -p 6380:6379 --restart=always -v /var/redis/data:/data  -d redis redis-server --appendonly yes
```


### fastdfs启动
```$xslt
docker run -d --network=host --restart=always --name tracker -v /var/fdfs/tracker:/var/fdfs delron/fastdfs tracker
docker run -d --network=host --restart=always --name storage -e TRACKER_SERVER=192.168.189.183:22122 -v /var/fdfs/storage:/var/fdfs -e GROUP_NAME=group1 delron/fastdfs storage
```
https://www.cnblogs.com/yanwanglol/p/9860202.html
上传图片

访问图片
http://192.168.136.170:8888/group1/M00/00/00/wKiIql2DHPqAQt7NAADU7JD7jqo181.png


## 6、运行

### 运行顺序

1. 运行AdminCenterProviderApplication
2. 运行AuthCenterProviderApplication
3. 运行FileCenterApplication（不上传文件可不启动）
4. 运行WorkCenterApplication（不使用工作流可不启动）
5. 运行MonitorApplication（开发环境可不启动）
5. 运行GatewayApplication


#### 打包
```
mvn -U -pl dcy-business/admin-center/admin-center-provider -am -DskipTests clean package
mvn -U -pl dcy-auth/auth-center-provider -am -DskipTests clean package
mvn -U -pl dcy-business/file-center -am -DskipTests clean package
mvn -U -pl dcy-business/workflow-center -am -DskipTests clean package
mvn -U -pl dcy-business/monitor-center -am -DskipTests clean package
mvn -U -pl dcy-gateway -am -DskipTests clean package
```
#### 构建镜像
```
cd dcy-business/admin-center/admin-center-provider
docker build -t admin-center -f Dockerfile .

cd dcy-auth/auth-center-provider
docker build -t auth-center -f Dockerfile .

cd dcy-business/file-center
docker build -t file-center -f Dockerfile .

cd dcy-business/workflow-center
docker build -t workflow-center -f Dockerfile .

cd dcy-business/monitor-center
docker build -t monitor-center -f Dockerfile .

cd dcy-api-gateway
docker build -t dcy-api-gateway -f Dockerfile .

```

#### docker单独启动
```
暂时不可使用，后续完善
docker run --name dcy-monitor -p 8764:8764 -d dcy-monitor
docker run --name dcy-auth-server -e ADMIN_CLIENT_URL=http://192.168.136.170:8764 -e MYSQL_URL=192.168.190.132 -p 7777:7777 -d dcy-auth-server
docker run --name dcy-api-gateway -e ADMIN_CLIENT_URL=http://192.168.136.170:8764 -p 9001:9001 -d dcy-api-gateway
docker run --name dcy-admin-center -e ADMIN_CLIENT_URL=http://192.168.136.170:8764 -e MYSQL_URL=192.168.190.132 -e AUTH_SERVER_ADDR=192.168.136.170:9001 -p 8999:8999 -d dcy-admin-center
docker run --name file-center -e ADMIN_CLIENT_URL=http://192.168.136.170:8764 -e MYSQL_URL=192.168.190.132 -p 5000:5000 -d file-center
```

#### docker-compose启动
```
docker-compose up -d
```
