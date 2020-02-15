# dcy-microservices-platform

<p align="center">
  <img src='https://img.shields.io/badge/license-Apache%202-4EB1BA.svg' alt='License'/>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.1.9.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.1.0.RELEASE-blue" alt="Downloads"/>
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
* 提供应用管理，方便第三方系统接入，**支持多租户(应用隔离)**
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
│  ├─dcy-api-gateway -- 第二代网关[9001]
│  ├─dcy-auth -- auth父级模块
│  │  ├─dcy-auth-common -- auth公共类
│  │  ├─dcy-auth-spring-boot-stater -- auth-stater
│  │  ├─dcy-auth-server -- 认证中心[7777]
│  │─dcy-business -- 父级模块
│  │  ├─dcy-modules -- 业务父模块
│  │  │  ├─dcy-admin-center -- 后台管理模块[8999]
│  │  │  ├─dcy-test-center -- 测试模块[8003]
│  │  ├─dcy-monitor -- 监控模块[8764]
│  │  ├─file-center -- 文件模块[5000]
│  │─dcy-commons -- 通用工具一级工程
│  │  ├─dcy-common-spring-boot-starter -- 封装通用操作逻辑
│  │  ├─dcy-db-spring-boot-starter -- 封装数据库通用操作逻辑
│  │  ├─dcy-feign-spring-boot-stater -- feign封装
│  │  ├─dcy-generator -- 代码生成器
│  │  ├─dcy-log-spring-boot-starter -- 封装log通用操作逻辑
│  │  ├─dcy-redis-spring-boot-starter -- 封装Redis通用操作逻辑
```

&nbsp;

## 5. 环境准备
nacos 基于二进制部署

redis集群或者单机、kafka、zipkin、sentinel-dashboard、ELK、zookeeper、fastdfs

基于docker安装

### nacos 启动


```$xslt
下载最新版本https://github.com/alibaba/nacos/releases
解压
首先修改配置文件，用数据库方式存储
nacos-server-1.1.3\conf\application.properties

尾行添加内容 对应自己的ip地址数据库
begin----
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://192.168.190.132:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=root
end----


cd nacos-server-1.1.3/bin
startup.cmd
```

### redis启动
```$xslt
redis启动
docker run -p 6380:6379 --restart=always -v /var/redis/data:/data  -d redis redis-server --appendonly yes
```

### kafka+zookeeper启动
创建 docker-compose.yml 文件
```$xslt
version: '2'
services:
  zoo1:
    image: wurstmeister/zookeeper
    restart: unless-stopped
    hostname: zoo1
    restart: always
    ports:
      - "2181:2181"
    container_name: zookeeper
  kafka:
    image: wurstmeister/kafka
    restart: always
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 192.168.136.170
      KAFKA_ADVERTISED_PORT: 9092
      KAFKA_ZOOKEEPER_CONNECT: "zoo1:2181"
      KAFKA_BROKER_ID: 1
    depends_on:
      - zoo1
    container_name: kafka
```
启动命令
```$xslt
docker-compose up -d
```

### zipkin启动
zipkin docker启动
地址：https://hub.docker.com/r/openzipkin/zipkin
使用得是kafka启动方式
```$xslt
version: '2'
services:
  zipkin:
    image: openzipkin/zipkin
    container_name: zipkin
    environment:
      - STORAGE_TYPE=elasticsearch
      - ES_HOSTS=http://192.168.136.170:9200
      #- ES_USERNAME=elastic
      #- ES_PASSWORD=changeme
      - ES_INDEX=zipkin
      - ES_DATE_SEPARATOR=-
      # Point the zipkin at the storage backend
      - COLLECTOR_KAFKA_ENABLED=true
      - KAFKA_BOOTSTRAP_SERVERS=192.168.136.170:9092
      - KAFKA_GROUP_ID=zipkin
      - KAFKA_TOPIC=zipkin
      # Uncomment to enable scribe
      # - SCRIBE_ENABLED=true
      # Uncomment to enable self-tracing
      # - SELF_TRACING_ENABLED=true
      # Uncomment to enable debug logging
      # - JAVA_OPTS=-Dlogging.level.zipkin2=DEBUG
    ports:
      # Port used for the Zipkin UI and HTTP Api
      - 9411:9411
      # Uncomment if you set SCRIBE_ENABLED=true
      # - 9410:9410
  dependencies:
    image: openzipkin/zipkin-dependencies
    container_name: dependencies
    entrypoint: crond -f
    environment:
      - STORAGE_TYPE=elasticsearch
      - ES_HOSTS=http://192.168.136.170:9200
      #- ES_USERNAME=elastic
      #- ES_PASSWORD=changeme
      - ES_INDEX=zipkin
      - ES_DATE_SEPARATOR=-
      # Uncomment to see dependency processing logs
      # - ZIPKIN_LOG_LEVEL=DEBUG
      # Uncomment to adjust memory used by the dependencies job
      # - JAVA_OPTS=-verbose:gc -Xms1G -Xmx1G

```
启动命令
```$xslt
docker-compose -f docker-compose-kafka-dcy.yml up -d
```

### sentinel-dashboard启动
```$xslt
docker run --name sentinel -d -p 8858:8858 -d bladex/sentinel-dashboard
```

### ELK启动
地址：https://github.com/deviantony/docker-elk.git

修改logstash.conf配置文件 连接到kafka

cd docker-elk/logstash/pipeline

```$xslt
input {
        kafka {
        bootstrap_servers => "192.168.136.170:9092"
        group_id => "dcy-microservices-platform-group"
        auto_offset_reset => "latest"
        consumer_threads => 5
        decorate_events => true
        topics => ["dcy-microservices-platform-topic"]
        type => "bhy"
     }
}

## Add your filters / logstash plugins configuration here

output {
        elasticsearch {
                hosts => "192.168.136.170:9200"
                index => "log-dcy-microservice-%{+YYYY.MM.dd}"
                #user => "elastic"
                #password => "changeme"
        }
}

```
运行elk
```$xslt
docker-compose up -d
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

1. 运行DcyMonitorApplication
2. 运行DcyAuthServerApplication
3. 运行DcyApiGatewayApplication
4. 运行AdminCenterProviderApplication
5. 运行FileCenterApplication


#### 打包
```
mvn -U -pl dcy-business/dcy-monitor -am -DskipTests clean package
mvn -U -pl dcy-auth/dcy-auth-server -am -DskipTests clean package
mvn -U -pl dcy-api-gateway -am -DskipTests clean package
mvn -U -pl dcy-business/dcy-modules/dcy-admin-center -am -DskipTests clean package
mvn -U -pl dcy-business/file-center -am -DskipTests clean package
```
#### 构建镜像
```
cd dcy-business/dcy-monitor
docker build -t dcy-monitor -f Dockerfile .

cd dcy-auth/dcy-auth-server
docker build -t dcy-auth-server -f Dockerfile .

cd dcy-api-gateway
docker build -t dcy-api-gateway -f Dockerfile .

cd dcy-business/dcy-modules/dcy-admin-center
docker build -t dcy-admin-center -f Dockerfile .

cd dcy-business/file-center
docker build -t file-center -f Dockerfile .

```

#### docker单独启动
```
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
