# LearnSpringCloud

SpringCloud Alibaba

[2020 SpringCloud(H版&alibaba)框架开发教程_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV18E411x7eT?from=search&seid=12947583133329416541)

## 1 SpringCloud Alibaba介绍

- SpringCloud Netflix 项目进入维护状态(maintenance)状态，不会再添加新功能，只修复Bug。2018年10月31日，SpringCloud Alibaba正式入驻Spring Cloud官方孵化器，并在Maven中央仓库发布第一个版本。

- 主要功能
  
  * **服务限流降级**：默认支持 WebServlet、WebFlux, OpenFeign、RestTemplate、Spring Cloud Gateway, Zuul, Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
  * **服务注册与发现**：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持。
  * **分布式配置管理**：支持分布式系统中的外部化配置，配置更改时自动刷新。
  * **消息驱动能力**：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。
  * **分布式事务**：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题。。
  * **阿里云对象存储**：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。
  * **分布式任务调度**：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。
  * **阿里云短信服务**：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。
* 主要组件：
  
  - **[Sentinel](https://github.com/alibaba/Sentinel)** ：把流量作为切入点，从流量控制、**熔断降级**、系统负载保护等多个维度保护服务的稳定性。
  
  - **[Nacos](https://github.com/alibaba/Nacos)** ：一个更易于构建云原生应用的动态**服务发现**、配置管理和服务管理平台。
  
  - **[RocketMQ](https://rocketmq.apache.org/)** ：一款开源的分布式**消息系统**，基于高可用分布式集群技术，提供低延时的、高可靠的消息发布与订阅服务。
  
  - **[Dubbo](https://github.com/apache/dubbo)** ：Apache Dubbo™ 是一款高性能** Java RPC **框架。
  
  - **[Seata](https://github.com/seata/seata)** ：阿里巴巴开源产品，一个易于使用的高性能微服务**分布式事务**解决方案。
  
  - **[Alibaba Cloud ACM](https://www.aliyun.com/product/acm)** ：一款在分布式架构环境中对应用配置进行集中管理和推送的应用配置中心产品。
  
  - **[Alibaba Cloud OSS](https://www.aliyun.com/product/oss)** : 阿里云对象存储服务（Object Storage Service，简称 OSS），是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和访问任意类型的数据。
  
  - **[Alibaba Cloud SchedulerX](https://help.aliyun.com/document_detail/43136.html)** : 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。
  
  - **[Alibaba Cloud SMS](https://www.aliyun.com/product/sms)** : 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。
  
  - 更多组件请参考 [Roadmap](https://github.com/alibaba/spring-cloud-alibaba/blob/master/Roadmap-zh.md)。

## 2 Nacos介绍

- Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。【**Nacos可以替代Eureka/Zookeeper，SpringCloud Config，Bus**】

### 2.1 Nacos安装

- 命令启动`sh startup.sh -m standalone`

- 访问 [Nacos](http://localhost:8848/nacos/#/login) ， 默认用户名和密码都是Nacos

> Nacos启动后直接就是一个运行着的服务。不用像Eureka那样单独设置一个或多个模块启动作为注册中心。

### 2.2 各种注册中心的对比

![O7iwrt](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/O7iwrt.png)

- **Nacos支持AP和CP模式的切换**
  
  - 简单地说，C就是所有节点在同一时间数据是一致地，A就是所有的请求都能收到
  
  - 既然支持CP和AP,那么如何选择?
  
  - 一般来说，**如果不需要存储服务级别的信息且服务实例是通过 nacos- client注册,并能够保持心跳上报,那么就可以选择AP模式**。当前主流的服务如 SpringCloud和 Dubbo服务,都适用于AP模式, AP模式为了服务的可能性而减弱了一致性,因此AP模式下只支持注册临时实例。
  
  - 如果需要在服务级别编辑或者存储配置信息,那么CP是必须,**K8S服务和DNS服务则适用于CP模式。** CP模式支持注册持久化实例,此时则是以Raft协议为集群运行模式,该模式下注册实例之前必须先注册服务,如果服务不存在,则会返回错误。

- 如何切换Nacos的CP和AP模式？`curl -X PUT '$NACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP'`

## 3 使用Nacos替代Eureka做服务注册中心

- 在父POM中添加`spring-cloud-alibaba-dependencies`依赖

- 在服务端添加`spring-cloud-starter-alibaba-nacos-discovery`依赖

### 3.1  将服务生产者注册进Nacos

- 1、POM中添加`spring-cloud-starter-alibaba-nacos-discovery`依赖

- 2、在主配置文件中配置Nacos：
  
  ```yaml
  server:
    port: 9001
  
  spring:
    application:
      name: nacos-payment-provider
    cloud:
      nacos:
        discovery:
          server-addr: 127.0.0.1:8848
  
  # 暴露监控端口
  management:
    endpoints:
      web:
        exposure:
          include: "*"
  ```

- 3、编写主启动类，标注`@EnableDiscoveryClient`将微服务注册到注册中心

- 4、启动：![0N81fW](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/0N81fW.png)

- 6、同理新建9002，也可以使用Idea的Copy Configuration来实现复制微服务：![iGFxLG](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/iGFxLG.png)

### 3.2 将服务消费者注册进Nacos

- 1、新建`cloudalibaba-consumer-order83`模块作为消费者，引入同样的POM

- 2、编写主配置文件：
  
  ```yml
  server:
    port: 83
  
  spring:
    application:
      name: nacos-order-consumer
    cloud:
      nacos:
        discovery:
          server-addr: 127.0.0.1:8848
  
  # 消费者将要去访问的微服务名称（注册成功进nacos的微服务提供者）
  service-url:
    nacos-user-service: http://nacos-payment-provider
  ```

- 3、编写主启动类，同样标注`@EnableDiscoveryClient`

- 4、编写Controller:
  
  ```java
  @RestController
  @Slf4j
  public class NacosOrderController {
  
      @Resource
      private RestTemplate restTemplate;
  
      // 实现从配置文件中读取微服务地址，实现代码和配置分离
      @Value("${service-url.nacos-user-service}") 
      private String serverUrl;
  
      @GetMapping(value = "/consumer/payment/nacos/{id}")
      public String paymentInfo(@PathVariable("id") Integer id){
          return restTemplate.getForObject(serverUrl+"/payment/nacos/" + id, String.class);
      }
  }
  ```

- 5、访问 http://127.0.0.1:83/consumer/payment/nacos/1 测试，显示的端口在9001和9002之间来回切换，证明负载均衡生效

> Nacos自带Ribbon实现负载均衡，但是在配置类中注入RestTemplage时要记得引入`@LoadBalanced`注解

## 4 使用Nacos替代SpringCloud Config做配置中心

### 4.1 建立Config Client模块

- 1、新建模块`cloudalibaba-config-nacos-client3377`, 引入`spring-cloud-starter-alibaba-nacos-config`POM依赖

- 2、 nacos-config-client需要2份配置文件。Bootstarp的优先级高于Application。​ 主要是为了可以与Spring Cloud Config无缝迁移.
  
  ```yml
  # bootstrap.yml
  server:
    port: 3377
  spring:
    application:
      name: nacos-config-client
    cloud:
      nacos:
        discovery:
          server-addr: 127.0.0.1:8848 # Nacos作为注册中心的地址
        config:
          server-addr: 127.0.0.1:8848 # Nacos作为配置中心的地址
          file-extension: yaml # 这里指定的文件格式需要和nacos上新建的配置文件后缀相同，否则读不到
          # group: DEV_GROUP
          # namespace:
  
  #  ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
  ```
  
  ```yml
  # application.yml
  # nacos注册配置
  spring:
    profiles:
      active: dev # 开发环境
      # active: test # 测试环境
      # active: pro # 正式环境
  ```

- 3、编写主启动类

- 4、编写控制器类，使用`@RefreshScope`注解支持**Nacos的动态刷新功能**
  
  ```java
  @RestController
  @RefreshScope // 支持Nacos的动态刷新功能
  public class ConfigClientController {
  
      @Value("${config.info}")
      private String configInfo;
  
      @GetMapping("/config/info")
      public String getConfigInfo(){
          return configInfo;
      }
  }
  ```

### 4.2 Nacos配置管理

- **配置文件的命名的规则**    ：`${prefix}-$(spring.profile.active).${file-extension}`
  
  - `prefix` 默认为 `spring.application.name` 的值，也可以通过配置项 `spring.cloud.nacos.config.prefix`来配置。
  * `spring.profile.active` 即为当前环境对应的 profile，详情可以参考 [Spring Boot文档](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html#boot-features-profiles)。 **注意：当 `spring.profile.active` 为空时，对应的连接符 `-` 也将不存在，dataId 的拼接格式变成 `${prefix}.${file-extension}`**
  * `file-exetension` 为配置内容的数据格式，可以通过配置项 `spring.cloud.nacos.config.file-extension` 来配置。目前只支持 `properties` 和 `yaml` 类型。
  - `${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}`
  - 例如`nacos-config-client-dev.yaml`
  - ![4NDb9T](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/4NDb9T.png)

- 1、在Nacos后台新建配置:
  
  - ![AXTE7y](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/AXTE7y.png)

- 2、启动client3377测试，访问 http://127.0.0.1:3377/config/info
  
  - ![CCqkiR](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/CCqkiR.png)

### 4.3 测试Nacos的动态刷新

- 1、在Nacos后台页面修改配置文件内容

- 2、再次访问，![9mT7Yz](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/9mT7Yz.png)

### 4.4 Nacos配置文件命名

- 命名方式： Namespace+ Group+ Data ID，类似于java的 包名 + 类名 + 方法名
  
  - ![VFDuKZ](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/VFDuKZ.png)

- 默认情况下：Namespace = public ,Group = DEFAULT_GROUP，默认Cluster是DEFAULT

- Namespace主要用来实现隔离，比如可以为开发、测试、生产环境创建三个不同的Namespace

### 4.5 修改配置文件读取不同Data Id对应的配置

- 可以在主配置文件中更改`spring.profile.active`属性的值来切换Nacos后台中发布的不同环境的配置文件
  
  - ![NhfEaT](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/NhfEaT.png)
  
  - ![o03nac](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/o03nac.png)

### 4.6 借助Group实现环境区分

- ![zFnbRc](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/zFnbRc.png)

- 在bootstrap.yaml中配置Group: ![MoeAdv](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/MoeAdv.png)

### 4.7 Namespace

- 新建命名空间：![iyQaVs](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/iyQaVs.png)

- 在dev命名空间中增加相应的配置文件，隶属于不同的Group：![](/Users/brightzh/Library/Application%20Support/marktext/images/2020-05-02-18-29-08-image.png)

## 5 Nacos集群配置

- Nacos支持三种部署模式
  
  - 单机模式 - 用于测试和单机试用。
  
  - 集群模式 - 用于生产环境，确保高可用。
  
  - 多集群模式 - 用于多数据中心场景。

- 配置Nacos集群不用再像Eureka一样需要我们新建多个模块

- 官方推荐的Nacos集群架构图：![C7yDGs](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/C7yDGs.png)

- ![42c1s5](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/42c1s5.jpg)
  
  - [http://ip1](http://ip1/):port/openAPI 直连ip模式，机器挂则需要修改ip才可以使用。
  
  - [http://VIP](http://vip/):port/openAPI 挂载VIP模式，直连vip即可，下面挂server真实ip，可读性不好。【**VIP表示虚拟IP**】
  
  - http://nacos.com/:port/openAPI 域名 + VIP模式，可读性好，而且换ip方便，推荐模式

- 默认的Nacos使用了嵌入的数据库[Apache Derby](http://db.apache.org/derby/)持久化数据，配置Nacos集群必须配置外部的MySQL做数据持久化，不然不同Nacos服务器使用各自的嵌入式数据库会造成数据同步问题。目前Nacos只支持MySQL做外部数据持久化。

### 5.1 配置Nacos支持MySQL

* 1.安装数据库，版本要求：5.6.5+

* 2.初始化mysql数据库，运行数据库初始化文件：`nacos-mysql.sql`【可以在目录nacos/conf下找到】
  
  * 注意：数据库名需要为`nacos_config`
  * ![fDLsFS](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/fDLsFS.png)

* 3.修改`conf/application.properties`文件，增加支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码。
  
  ```yml
  spring.datasource.platform=mysql
  db.num=1
  db.url.0=jdbc:mysql://11.162.196.16:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
  db.user=nacos_devtest
  db.password=youdontknow
  ```
- 4、MySQL配置完毕之后再次启动时应该使用单机模式启动，否则会报错: `sh startup.sh -m standalone`
  
  - [nacos的单机和集群启动一些问题 - 简书](https://www.jianshu.com/p/0cae36a8b3da)

### 5.2 Nacos集群的配置步骤

> 3个或3个以上Nacos才能部署集群

- 使用1个Nginx， 3个Nacos注册中心和1个MySQL
  
  - [Nginx for Mac install、start、stop 、reload实践 - 知乎](https://zhuanlan.zhihu.com/p/38485095)

- 编写`cluster.conf`
  
  - ![hzM8GL](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/hzM8GL.png)
  
  - 注意：这里的例子仅用于本地学习测试使用，实际生产环境必须部署在不同的节点上，才能起到高可用的效果。另外，Nacos的集群需要3个或3个以上的节点，并且确保这三个节点之间是可以互相访问的。

- 修改Nacos的启动脚本`startup.sh`来实现启动不同端口的Nacos实例：
  
  - 在参数列表里添加`-p`
  
  - 在`nohup $JAVA`后面新增`-Dserver.port=${PORT}`这句话前后有空格
    
    ```shell
      #!/bin/sh
    
      # Copyright 1999-2018 Alibaba Group Holding Ltd.
      # Licensed under the Apache License, Version 2.0 (the "License");
      # you may not use this file except in compliance with the License.
      # You may obtain a copy of the License at
    
      #      http://www.apache.org/licenses/LICENSE-2.0
      #
      # Unless required by applicable law or agreed to in writing, software
      # distributed under the License is distributed on an "AS IS" BASIS,
      # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      # See the License for the specific language governing permissions and
      # limitations under the License.
    
      cygwin=false
      darwin=false
      os400=false
      case "`uname`" in
      CYGWIN*) cygwin=true;;
      Darwin*) darwin=true;;
      OS400*) os400=true;;
      esac
      error_exit ()
      {
          echo "ERROR: $1 !!"
          exit 1
      }
      [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
      [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
      [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/opt/taobao/java
      [ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME
    
      if [ -z "$JAVA_HOME" ]; then
        if $darwin; then
    
          if [ -x '/usr/libexec/java_home' ] ; then
            export JAVA_HOME=`/usr/libexec/java_home`
    
          elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
            export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
          fi
        else
          JAVA_PATH=`dirname $(readlink -f $(which javac))`
          if [ "x$JAVA_PATH" != "x" ]; then
            export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
          fi
        fi
        if [ -z "$JAVA_HOME" ]; then
              error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
        fi
      fi
    
      export SERVER="nacos-server"
      export MODE="cluster"
      export FUNCTION_MODE="all"
      while getopts ":m:f:s:p:" opt
      do
          case $opt in
              m)
                  MODE=$OPTARG;;
              f)
                  FUNCTION_MODE=$OPTARG;;
              s)
                  SERVER=$OPTARG;;
              p)
                  PORT=$OPTARG;;
              ?)
              echo "Unknown parameter"
              exit 1;;
          esac
      done
    
      export JAVA_HOME
      export JAVA="$JAVA_HOME/bin/java"
      export BASE_DIR=`cd $(dirname $0)/..; pwd`
      export DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
      export CUSTOM_SEARCH_LOCATIONS=${DEFAULT_SEARCH_LOCATIONS},file:${BASE_DIR}/conf/
    
      #===========================================================================================
      # JVM Configuration
      #===========================================================================================
      if [[ "${MODE}" == "standalone" ]]; then
          JAVA_OPT="${JAVA_OPT} -Xms512m -Xmx512m -Xmn256m"
          JAVA_OPT="${JAVA_OPT} -Dnacos.standalone=true"
      else
          JAVA_OPT="${JAVA_OPT} -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
          JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/java_heapdump.hprof"
          JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"
    
      fi
    
      if [[ "${FUNCTION_MODE}" == "config" ]]; then
          JAVA_OPT="${JAVA_OPT} -Dnacos.functionMode=config"
      elif [[ "${FUNCTION_MODE}" == "naming" ]]; then
          JAVA_OPT="${JAVA_OPT} -Dnacos.functionMode=naming"
      fi
    
      JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
      if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]] ; then
        JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=${BASE_DIR}/logs/nacos_gc.log:time,tags:filecount=10,filesize=102400"
      else
        JAVA_OPT="${JAVA_OPT} -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext"
        JAVA_OPT="${JAVA_OPT} -Xloggc:${BASE_DIR}/logs/nacos_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
      fi
    
      JAVA_OPT="${JAVA_OPT} -Dloader.path=${BASE_DIR}/plugins/health,${BASE_DIR}/plugins/cmdb,${BASE_DIR}/plugins/mysql"
      JAVA_OPT="${JAVA_OPT} -Dnacos.home=${BASE_DIR}"
      JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/target/${SERVER}.jar"
      JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"
      JAVA_OPT="${JAVA_OPT} --spring.config.location=${CUSTOM_SEARCH_LOCATIONS}"
      JAVA_OPT="${JAVA_OPT} --logging.config=${BASE_DIR}/conf/nacos-logback.xml"
      JAVA_OPT="${JAVA_OPT} --server.max-http-header-size=524288"
    
      if [ ! -d "${BASE_DIR}/logs" ]; then
        mkdir ${BASE_DIR}/logs
      fi
    
      echo "$JAVA ${JAVA_OPT}"
    
      if [[ "${MODE}" == "standalone" ]]; then
    
          echo "nacos is starting with standalone"
    
      else
    
          echo "nacos is starting with cluster"
    
      fi
    
    # check the start.out log output file
    
      if [ ! -f "${BASE_DIR}/logs/start.out" ]; then
        touch "${BASE_DIR}/logs/start.out"
      fi
    
    # start
    
      echo "$JAVA ${JAVA_OPT}" > ${BASE_DIR}/logs/start.out 2>&1 &
      nohup $JAVA -DServer.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &
      echo "nacos is starting，you can check the ${BASE_DIR}/logs/start.out"
    ```
  
  - 以后启动Nacos实例时只要使用`./shartup.sh -p 3333`就可以运行一个3333端口的Nacos

- 配置Nginx:
  
  ```
    #user  nobody;
    worker_processes  1;
  
    events {
        worker_connections  1024;
    }
  
    http {
        include       mime.types;
        default_type  application/octet-stream;
        sendfile        on;
  
        #keepalive_timeout  0;
        keepalive_timeout  65;
  
        #gzip  on;
  
        upstream cluster{
            server 192.168.99.114:3333;
            server 192.168.99.114:4444;
            server 192.168.99.114:5555;
        }
  
        server {
            listen       1111;
            server_name  localhost;
  
            #charset koi8-r;
  
            #access_log  logs/host.access.log  main;
  
            location / {
                #root   html;
                #index  index.html index.htm;
                proxy_pass http://cluster;
            }
  
            error_page   500 502 503 504  /50x.html;
            location = /50x.html {
                root   html;
            }
        }
        include servers/*;
  
    }
  ```

- 启动并测试：
  
  - 使用`./shartup.sh -p xxxx`，运行3333,4444,5555端口的Nacos集群
  
  - 启动Nginx: ` /usr/local/Cellar/nginx/1.15.0/bin/nginx -c /usr/local/etc/nginx/nginx.conf `  -c 指定启动配置文件
    
    - [nginx 命令笔记(mac) - 简书](https://www.jianshu.com/p/36bc3728cb44)
    
    - `nginx` 用默认配置文件启动
    
    - `nginx -s stop` 关闭nginx
    
    - `nginx-s reload` 重启nginx
    
    - `nginx -t` 检查nginx配置文件语法是否正确
  
  - 检查Nacos日志`/nacos-1.2.1/logs/start.out`，查看集群启动是否报错
  
  - 依次访问 http://127.0.0.1/3333/nacos , 4444, 5555查看单个的Nacos是否运行成功
  
  - 访问Nginx的代理地址 http//127.0.0.1:1111/nacos  查看Nginx是否代理成功
  
  - 添加配置，查看MySQL表中是否新建了对应字段

### 5.3 将微服务注册进Nacos集群

- 1、修改9001的配置文件：
  
  ```java
    server:
      port: 9001
  
    spring:
      application:
        name: nacos-payment-provider
      cloud:
        nacos:
          discovery:
            # 单机版
            # server-addr: 127.0.0.1:8848
            # 集群版
            server-addr: 127.0.0.1:1111
  
    # 暴露监控端口
    management:
      endpoints:
        web:
          exposure:
            include: "*"
  ```

- 2、查看Nacos后台![FQFqg3](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/FQFqg3.png)

- ![8GIHT0](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/8GIHT0.png)

## 6 Sentinel 实现流量控制

- [Sentinel](https://yq.aliyun.com/go/articleRenderRedirect?url=https%3A%2F%2Fgithub.com%2Falibaba%2FSentinel) 是阿里中间件团队研发的面向分布式服务架构的轻量级高可用流量控制组件，最近正式开源。Sentinel 主要以流量为切入点，从**流量控制、熔断降级、系统负载保护**等多个维度来帮助用户保护服务的稳定性。

- ![TIQho3](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/TIQho3.jpg)

- Sentinel 分为两个部分:
  
  * 核心库（Java 客户端）不依赖任何框架/库，能够运行于所有 Java 运行时环境，同时对 Dubbo / Spring Cloud 等框架也有较好的支持。
  * 控制台（Dashboard）基于 Spring Boot 开发，打包后可以直接运行，不需要额外的 Tomcat 等应用容器。

### 6.1 Sentinel和Hystrix对比

- 使用Hystrix时需要我们手工新建一个Hystrix模块，并且需要`cloud-consumer-hystrix-dashboard9001`模块来配置后台监控。

- Hystrix的后台监控功能有限，没有一套功能完整的Web后台来实现细粒度的配置（流控、速率控制、服务熔断、服务降级）

- Sentinel单独一个组件可以独立出来，支持界面化的细粒度统一配置

### 6.2 Sentinel的安装和简单使用

- 1 、下载jar包直接运行，默认端口8080，访问[Sentinel Dashboard](http://localhost:8080/#/login)，默认账户名和密码都是sentinel

- 2、新建模块`cloudalibaba-sentinel-service8401`被Sentinel保护
  
  - 引入`spring-cloud-starter-alibaba-sentinel`和`sentinel-datasource-nacos`（做持久化）的POM依赖
  
  - 编写主配置文件:
    
    ```yml
    server:
      port: 8401
    
    spring:
      application:
        name: cloudalibaba-sentinel-service
      cloud:
        nacos:
          discovery:
            server-addr: 127.0.0.1:8848
        sentinel:
          transport:
            dashboard: 192.168.0.112:8080
            # 默认为8719，如果被占用会自动+1，直到找到为止
            port: 8719
    
    management:
      endpoints:
        web:
          exposure:
            include: "*"
    ```
  
  - 编写主启动类，启动8401模块

- 3、测试Sentinel监控：因为Sentinel使用的是懒加载机制，必须访问一次8401模块才能使8401被Sentinel监控到

- 4、编写8401的Controller类：

- 5、访问8401的服务 http://127.0.0.1:8401/testB , 再次登陆Sentinel后台：![ToVVFU](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/ToVVFU.png)

### 6.3 Sentinel的流控规则介绍

- 资源名:唯名称,默认请求路径

- 针对来源: Sentinel可以针对调用者进行限流,填写微服务名,默认default(不区分来源)

- 阈值类型/单机阈值
  
  - QPS(每秒钟的请求数量):当调用该api的QPS达到阈值的时候,进行限流
  
  - 线程数:当调用该api的线程数达到阈值的时候,进行限流

- 流控模式：
  
  - 直接:api达到限流条件时,直接限流
  
  - 关联:当关联的资源达到阈值时,就限流自己
  
  - 链路:只记录指定链路上的流量(指定资源从入口资源进来的流量,如果达到阈值,就进行限流)【api级别的针对来源】

- 流控效果：
  
  - 快速失败:声接失败,抛异常
  
  - Warm Up:根据 codeFactor (冷加载因子,默认3)的值,从阈值 codeFactor,经过预热时长,才达到设置的QPS阈值
  
  - 排队等待：匀速排队，让请求以匀速的速度通过，阈值类型必须设置为QPS，否则无效

#### 6.3.1 QPS:直接 -> 快速失败

- `QPS`(每秒钟的请求数量): req/sec，当调用该api的QPS达到阈值的时候,进行限流。

- ![T1z3k2](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/T1z3k2.png)

- ![DPecsh](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/DPecsh.png)

- 直接调用的是默认提示信息，如何自定义提示信息？

#### 6.3.2 线程数限流

- 线程数:当调用该api的线程数达到阈值的时候,进行限流

- 线程数限流没有流控效果选项：![czqNep](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/czqNep.png)

#### 6.3.3 关联

- 当关联的资源达到阈值，流控自己。例如当与请求资源A关联的资源B达到阈值后，就限流A（相当于B生病，A吃药）
  
  - 作用：如果B是支付接口，A是下订单的接口。如果B请求超过阈值之后，就限流下订单的A接口

- ![w0qgZE](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/w0qgZE.png)
  
  - 当/testB达到阈值之后会导致/testA不可用
  
  - 使用Postman来模拟并发密集访问/testB
  
  - ![M3fGwn](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/M3fGwn.png)
  
  - ![cZ9SYO](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/cZ9SYO.png)
  
  - 可以发现当大量线程访问/testB之后/testA反而被限流了

#### 6.3.4 链路

- 一棵典型的调用树如下图所示：

```
                   machine-root
                    /       \
                   /         \
             Entrance1     Entrance2
                /             \
               /               \
      DefaultNode(nodeA)   DefaultNode(nodeA)
```

- `NodeSelectorSlot` 中记录了资源之间的调用链路，这些资源通过调用关系，相互之间构成一棵调用树。这棵树的根节点是一个名字为 `machine-root` 的虚拟节点，调用链的入口都是这个虚节点的子节点。

- 上图中来自入口 `Entrance1` 和 `Entrance2` 的请求都调用到了资源 `NodeA`，**Sentinel 允许只根据某个入口的统计信息对资源限流**。比如我们可以设置 `FlowRule.strategy` 为 `RuleConstant.CHAIN`，同时设置 `FlowRule.ref_identity` 为 `Entrance1` 来表示只有从入口 `Entrance1` 的调用才会记录到 `NodeA` 的限流统计当中，而对来自 `Entrance2` 的调用漠不关心。
  
  调用链的入口是通过 API 方法 `ContextUtil.enter(name)` 定义的。

- ![4LrfvV](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/4LrfvV.png)

#### 6.3.5 Postman压测

- [用 Postman 做接口自动化（六）Collection Runner 运行参数设置_网络_小满测试-CSDN博客](https://blog.csdn.net/minzhung/article/details/102502266)

- [【Postman】10 Postman Runner的使用 - 知乎](https://zhuanlan.zhihu.com/p/109078237)

- 

- ![4pd9Nm](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/4pd9Nm.jpg)

- **Iterations**： 用例迭代的次数，也就是当前选中的这些请求需要运行几次；

- **Delay**延迟，用来设置每个请求之间的运行时间（以毫米为单位），如果设置了，则一个请求运行完后会等待相应的时间才运行下一个请求；

- **Log Response**记录响应日志，这是一种限制性的设置，默认是记录所有请求的日志，也可以限制为只记录错误日志或者完全不记录；

- **Data**选中数据文件，这是 Postman 提供的数据驱动的方式，数据针对当前 Collection 中请求中使用的变量。支持 CSV 和 Json 格式的文件；

- **Keep variable values**保持变量值。如果 Collection 中有脚本重新设置环境变量或者全局变量的值，默认情况下只对当次运行有效。如果勾选了此选项，那么在脚本中重设的变量值会保存下来，也就是会直接修改 Postman 中预设的变量值；

- **Run collection without using stored cookies**如果勾选此选项，运行 Collection 的时候则不会使用 Postman 的 cookie 管理器；

- **Save cookies after collection run**运行后，储存运行过程中的 cookies，此选项默认勾选。

### 6.4 流控效果

#### 6.4.1 快速失败

- 见前面的例子，访问时直接提示 `Blocked by Sentinel (flow limiting)`

- 直接失败，并抛出异常

#### 6.4.2 Warm-up 预热

- [流量控制 · alibaba/Sentinel Wiki](https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6)

- [限流 冷启动 · alibaba/Sentinel Wiki](https://github.com/alibaba/Sentinel/wiki/%E9%99%90%E6%B5%81---%E5%86%B7%E5%90%AF%E5%8A%A8)

- Warm Up（`RuleConstant.CONTROL_BEHAVIOR_WARM_UP`）方式，即预热/冷启动方式。当系统长期处于低水位的情况下，当流量突然增加时，直接把系统拉升到高水位可能瞬间把系统压垮。通过"冷启动"，让通过的流量缓慢增加，在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮。详细文档可以参考 [流量控制 - Warm Up 文档](https://github.com/alibaba/Sentinel/wiki/%E9%99%90%E6%B5%81---%E5%86%B7%E5%90%AF%E5%8A%A8)，具体的例子可以参见 [WarmUpFlowDemo](https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/flow/WarmUpFlowDemo.java)。
  
  - ![Rzp0Gv](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/Rzp0Gv.jpg)

- 公式：阈值除以 $coldFactor$（冷加载因子，默认为3），即请求QPS从"阈值/3"开始，经过预热时长后才会达到阈值
  
  - ![pmhjAE](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/pmhjAE.png)
  
  - 意思是：系统初始化的阈值为6/3=2，即开始时的阈值为2；经过预热时长5秒后，QPS的阈值才从2提高到6
  
  - 访问 http://127.0.0.1:8401/testB ， 刚开始2秒内多次访问频繁出现报错，经过5s后频繁访问不会再报错

- 应用场景：秒杀系统

#### 6.4.3 匀速排队等待

- [流量控制 匀速排队模式 · alibaba/Sentinel Wiki](https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6-%E5%8C%80%E9%80%9F%E6%8E%92%E9%98%9F%E6%A8%A1%E5%BC%8F)

- 匀速排队（`RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER`）方式会**严格控制请求通过的间隔时间**，也即是让请求以均匀的速度通过，对应的是漏桶算法。详细文档可以参考 [流量控制 - 匀速器模式](https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6-%E5%8C%80%E9%80%9F%E6%8E%92%E9%98%9F%E6%A8%A1%E5%BC%8F)，具体的例子可以参见 [PaceFlowDemo](https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/flow/PaceFlowDemo.java)。
  
  - ![03INQ6](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/03INQ6.jpg)

- 这种方式主要用于**处理间隔性突发的流量，例如消息队列**。想象一下这样的场景，**在某一秒有大量的请求到来，而接下来的几秒则处于空闲状态，我们希望系统能够在接下来的空闲期间逐渐处理这些请求，而不是在第一秒直接拒绝多余的请求**。

- ![RbTCWr](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/RbTCWr.png)
  
  - 含义：/testA每秒只处理1次请求，超过的话就排队等待，等待的超时时间为20s。
  
  - 注意：排队等待只允许使用QPS作为阈值
  
  - 依然使用Postman作为压测：![zODqVI](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/zODqVI.png)
  
  - 结果：在控制台每1s打印一个结果：![2O9Z6a](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/2O9Z6a.png)
  
  - 被流控的请求的响应时间在均在1s左右：![VZjEjK](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/VZjEjK.png)

## 7 Sentinel实现服务降级

- [熔断降级 · alibaba/Sentinel Wiki](https://github.com/alibaba/Sentinel/wiki/%E7%86%94%E6%96%AD%E9%99%8D%E7%BA%A7)

- 除了流量控制以外，对**调用链路中不稳定的资源进行熔断降级**也是保障高可用的重要措施之一。由于调用关系的复杂性，如果调用链路中的某个资源不稳定，最终会导致请求发生堆积。**Sentinel 熔断降级会在调用链路中某个资源出现不稳定状态时（例如调用超时或异常比例升高），对这个资源的调用进行限制，让请求快速失败，避免影响到其它的资源而导致级联错误**。当资源被降级后，在接下来的降级时间窗口之内，对该资源的调用都自动熔断（默认行为是抛出 `DegradeException`）。

- **降级策略**：
  
  - **平均响应时间** (`DEGRADE_GRADE_RT`)：当 1s 内持续进入 N 个请求，对应时刻的平均响应时间（秒级）均超过阈值（`count`，以 ms 为单位），那么在接下来的时间窗口（`DegradeRule` 中的 `timeWindow`，以 s 为单位）之内，对这个方法的调用都会自动地熔断（抛出 `DegradeException`）。
    
    - 注意 Sentinel 默认统计的 RT 上限是 4900 ms，**超出此阈值的都会算作 4900 ms**，若需要变更此上限可以通过启动配置项 `-Dcsp.sentinel.statistic.max.rt=xxx` 来配置。
  * **异常比例** (`DEGRADE_GRADE_EXCEPTION_RATIO`)：当资源的每秒请求量 >= N（可配置），并且每秒异常总数占通过量的比值超过阈值（`DegradeRule` 中的 `count`）之后，资源进入降级状态，即在接下的时间窗口（`DegradeRule` 中的 `timeWindow`，以 s 为单位）之内，对这个方法的调用都会自动地返回。
    
    * 异常比率的阈值范围是 `[0.0, 1.0]`，代表 0% - 100%。
  
  * **异常数** (`DEGRADE_GRADE_EXCEPTION_COUNT`)：当资源近 1 分钟的异常数目超过阈值之后会进行熔断。
    
    * 注意由于统计时间窗口是分钟级别的，若 `timeWindow` 小于 60s，则结束熔断状态后仍可能再进入熔断状态。

- **Sentinel的熔断器是没有半开状态的**

### 7.1 配置`RT`策略的服务降级

- **平均响应时间RT策略**：1s内持续收到N个请求，且平均响应时间 > 给定的阈值则触发断路器打开，等待时间窗口结束之后关闭断路器，服务恢复

- 增加控制层方法：
  
  ```java
      @GetMapping("/testD")
      public String testD(){
          // testD每次需要1秒钟
          try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
          log.info(Thread.currentThread().getName() + "...testD ");
          return "testD -----";
      }
  ```

- 在Sentinel后台配置RT服务降级：
  
  - ![Te9X8X](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/Te9X8X.png)
  
  - 含义：如果平均请求响应时间超过200ms，则在2s内触发断路器。之后才恢复。
  
  - 由于之前在Controller中的方法会等待1000ms才返回数据，因此一定会触发RT降级。
  
  - ![EhzzXy](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/EhzzXy.png)
  
  - Jmeter 永远一秒钟打进来10个线程(大于5个了)调用/testD, 我们希望200毫秒处理完本次任务, 结果超过200毫秒还没处理完,在未来1秒钟的时间窗口内,断路器打开保险丝跳闸微服务不可用
  
  - 结果：在Jmeter10个线程并发访问触发断路器之后，再次手动访问报异常：`Blocked by Sentinel (flow limiting)`

### 7.2 配置`异常比例`策略的服务降级

- **异常比例** ：访问某个请求的QPS>N 并且异常比例（秒级比例，统计1s内的异常次数比例）如果超过设定的阈值，则触发断路器

- 异常比率的阈值范围是 `[0.0, 1.0]`，代表 0% - 100%。

- 在Sentinel后台配置异常比例服务降级：![RdmFY7](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/RdmFY7.png)
  
  - 在控制器方法中增加除零异常int a = 10 /0;
  
  - 使用Jmeter并发访问，由于每次请求都会发生异常，所以服务马上被熔断
  
  - 一开始访问 http://127.0.0.1:8401/testD 时提示 `# Whitelabel Error Page There was an unexpected error (type=Internal Server Error, status=500)./ by zero`
  
  - 触发断路器后再访问提示:`Blocked by Sentinel (flow limiting)`

### 7.3 配置`异常数`策略的服务降级

- **异常数策略**：当资源近 1 分钟的异常数目超过阈值之后会进行熔断。
  
  - 注意由于统计时间窗口是分钟级别的，若 `timeWindow` 小于 60s，则结束熔断状态后仍可能再进入熔断状态
  
  - 因为统计窗口是60s，因此恢复的时间窗口最好设置为60s以上

- ![nfw1rJ](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/nfw1rJ.png)
  
  - /testE中同样是除零异常
  
  - 访问 /testE 五次之后进入降级，70s后熔断解除，重新开始统计

## 8 Sentinel 热点key 限流

- 何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的 Top K 数据，并对其访问进行限制。比如：
  
  - 商品 ID 为参数，统计一段时间内最常购买的商品 ID 并进行限制
  - 用户 ID 为参数，针对一段时间内频繁访问的用户 ID 进行限制

- 热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，**对包含热点参数的资源调用进行限流**。热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。
  
  - ![SUL4Tu](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/SUL4Tu.jpg)

### 8.1 配置hotkey热点限流(@SentinelResource注解)

- 8401模块增加testHotkey方法：

```java
    // 测试热点限流
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "dealTestHotKey") // 资源标志名
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2){
        // int age = 10 /0;
        return "testHotKey -----";
    }

    public String dealTestHotKey(String p1, String p2, BlockException blockException){
        return p1 + "-" + p2 + "dealTestHotKey---------";
    }
```

- `@SentinelResource`注解中：
  
  - 类似于Hystrix中的中的@HystrixCommand注解
  
  - 属性value指定资源的标识名
  
  - 属性blockHandler指定“兜底方法” （更改原来的`Blocked by Sentinel (flow limiting)` 异常提示）

- ![Dxm679](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/Dxm679.png)
  
  - 含义：请求方法/testHotKey时如果携带了第一个参数的请求QPS超过1，马上触发降级处理，使用blockHandler中指定的方法做降级提示
  
  - ![G8uwD0](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/G8uwD0.png)
  
  - 使用了热点限流后如果不指明blockHandler，则默认会直接返回错误页面，很不友好![kQ9Wsf](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/kQ9Wsf.jpg)
  
  - 注意：如果请求参数中不包含热点参数p1, 例如访问 http://127.0.0.1:8401/testHotKey?p2=b ，则无论访问多么频繁都不会触发热点参数限流

### 8.2 参数例外项

- 之前我们的配置是只要请求参数中有p1，并且QPS超过阈值之后就马上触发热点限流。

- 假如我们希望如果请求参数中p1的值是某个特定值，那么他的阈值可以和全局阈值不一致，如何设置？

- ![UfgXe4](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/UfgXe4.png)
  
  - 含义：假如p1参数的值为5的话，则其请求的QPS阈值可以是200
  
  - 效果： 访问 http://127.0.0.1:8401/testHotKey?p1=5&p2=b 不会触发服务降级

### 8.3 异常与Sentinel限流的关系

>  `@SentinelResource`处理的是 Sentinel控制台配置的违规情况,有 blockHandler方法配置的兜底处理

> `RuntimeException` int age=10/0,这个是java运行时报出的运行时异常 RunTime Exception, **@Sentinelresource不管**

- 总结：Sentinelresource主管配置出错,运行出错该走异常走异常

## 9 Sentinel 系统保护机制

- Sentinel 系统自适应保护**从整体维度**对应用入口流量进行控制，结合应用的 Load、总体平均 RT、入口 QPS 和线程数等几个维度的监控指标，让系统的入口流量和系统的负载达到一个平衡，让系统尽可能跑在最大吞吐量的同时保证系统整体的稳定性。

- 相当于除了对某个资源或者热点进行限流之外，在所有Sentinel管理的服务之外，又包了一层“保护机制”，能根据整个系统整体状况进行流量控制。

### 9.1 系统保护原理

先用经典图来镇楼:

![TCP-BBR-pipe](https://user-images.githubusercontent.com/9434884/50813887-bff10300-1352-11e9-9201-437afea60a5a.png)

我们把系统处理请求的过程想象为一个水管，到来的请求是往这个水管灌水，当系统处理顺畅的时候，请求不需要排队，直接从水管中穿过，这个请求的RT是最短的；反之，当请求堆积的时候，那么处理请求的时间则会变为：排队时间 + 最短处理时间。

- 推论一: 如果我们能够保证水管里的水量，能够让水顺畅的流动，则不会增加排队的请求；也就是说，这个时候的系统负载不会进一步恶化。

我们用 T 来表示(水管内部的水量)，用RT来表示请求的处理时间，用P来表示进来的请求数，那么一个请求从进入水管道到从水管出来，这个水管会存在 `P * RT`　个请求。换一句话来说，当 `T ≈ QPS * Avg(RT)` 的时候，我们可以认为系统的处理能力和允许进入的请求个数达到了平衡，系统的负载不会进一步恶化。

接下来的问题是，水管的水位是可以达到了一个平衡点，但是这个平衡点只能保证水管的水位不再继续增高，但是还面临一个问题，就是在达到平衡点之前，这个水管里已经堆积了多少水。如果之前水管的水已经在一个量级了，那么这个时候系统允许通过的水量可能只能缓慢通过，RT会大，之前堆积在水管里的水会滞留；反之，如果之前的水管水位偏低，那么又会浪费了系统的处理能力。

- 推论二:　当保持入口的流量是水管出来的流量的最大的值的时候，可以最大利用水管的处理能力。

然而，和 TCP BBR 的不一样的地方在于，还需要用一个系统负载的值（load1）来激发这套机制启动。

> 注：这种系统自适应算法对于低 load 的请求，它的效果是一个“兜底”的角色。**对于不是应用本身造成的 load 高的情况（如其它进程导致的不稳定的情况），效果不明显。**

### 9.2 Sentinel支持的系统规则

- 系统规则支持以下的阈值类型：
  
  ![GzHefK](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/GzHefK.png)
  
  * **Load**（仅对 Linux/Unix-like 机器生效）：当系统 load1 超过阈值，且系统当前的并发线程数超过系统容量时才会触发系统保护。系统容量由系统的 `maxQps >md.png COPYING Config.plist CopyAsMarkdown-demo.mp4 README.md _Signature.plist html2md.sh html2text.py minRt` 计算得出。设定参考值一般是 `CPU cores >md.png COPYING Config.plist CopyAsMarkdown-demo.mp4 README.md _Signature.plist html2md.sh html2text.py 2.5`。
  * **CPU usage**（1.5.0+ 版本）：当系统 CPU 使用率超过阈值即触发系统保护（取值范围 0.0-1.0）。
  * **RT**：当单台机器上所有入口流量的平均 RT 达到阈值即触发系统保护，单位是毫秒。
  * **线程数**：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护。
  * **入口 QPS**：当单台机器上所有入口流量的 QPS 达到阈值即触发系统保护。

## 10 自定义限流处理逻辑

- 之前兜底方案面临的问题：
  
  - 若使用系统默认的限流提示，不能满足我们的业务要求
  
  - 如果为每一个方法都配置一个blockHandler兜底方法则会造成代码的膨胀，且和业务代码耦合在一起
  
  - 没有体现全局的处理思想

- 创建一个自定义处理类:

```java
public class MyHandler {
    // 注意一定要static
    public static CommonResult<Payment> handlerException1(BlockException e) {
        return new CommonResult<>(444, "按照用户自定义1，全局的", new Payment(2020L, "serial004----1"));
    }

    public static CommonResult<Payment> handlerException2(BlockException e) {
        return new CommonResult<>(444, "按照用户自定义2，全局的", new Payment(2020L, "serial004----2"));
    }
}
```

- 新建一个业务方法使用自定义的处理类，其中：
  
  - `blockHandlerClass`指定使用哪一个自定义处理类，这里指定上面的MyHandler
  
  - `blockHandler`指定该自定义类中的哪一个方法

```java
    //CustomBlockHandler
    @RequestMapping(value = "/rateLimit/customBlock", method = RequestMethod.GET)
    @SentinelResource(value = "customBlock", blockHandlerClass = MyHandler.class, blockHandler = "handlerException2")
    public CommonResult<Payment> customBlockHandler() {
        return new CommonResult<>(200, "按照客户自定义限流成功！", new Payment(200L, "serial003"));
    }
```

- 设置QPS为1 ，再次访问[127.0.0.1:8401/rateLimit/customBlock](http://127.0.0.1:8401/rateLimit/customBlock) ，自定义的限流方法被调用：![awVoN6](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/awVoN6.png)

- 各个方法之间的关系：![QwwPqu](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/QwwPqu.jpg)

## 12 @SentinelResource 注解

Sentinel 提供了 `@SentinelResource` 注解用于定义资源，并提供了 AspectJ 的扩展用于自动定义资源、处理 `BlockException` 等。使用 [Sentinel Annotation AspectJ Extension](https://github.com/alibaba/Sentinel/tree/master/sentinel-extension/sentinel-annotation-aspectj) 的时候需要引入以下依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-annotation-aspectj</artifactId>
    <version>x.y.z</version>
</dependency>
```

`@SentinelResource` 用于定义资源，并提供可选的异常处理和 fallback 配置项。 `@SentinelResource` 注解包含以下属性：

- `value`：资源名称，必需项（不能为空）
- `entryType`：entry 类型，可选项（默认为 `EntryType.OUT`）
- `blockHandler` / `blockHandlerClass`: `blockHandler` 对应处理 `BlockException` 的函数名称，可选项。blockHandler 函数访问范围需要是 `public`，返回类型需要与原方法相匹配，参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 `BlockException`。blockHandler 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 `blockHandlerClass` 为对应的类的 `Class` 对象，注意对应的函数必需为 static 函数，否则无法解析。
- `fallback`：fallback 函数名称，可选项，用于在抛出异常的时候提供 fallback 处理逻辑。fallback 函数可以针对所有类型的异常（除了 `exceptionsToIgnore` 里面排除掉的异常类型）进行处理。fallback 函数签名和位置要求：
  - 返回值类型必须与原函数返回值类型一致；
  - 方法参数列表需要和原函数一致，或者可以额外多一个 `Throwable` 类型的参数用于接收对应的异常。
  - fallback 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 `fallbackClass` 为对应的类的 `Class` 对象，注意对应的函数必需为 static 函数，否则无法解析。
- `defaultFallback`（since 1.6.0）：默认的 fallback 函数名称，可选项，通常用于通用的 fallback 逻辑（即可以用于很多服务或方法）。默认 fallback 函数可以针对所以类型的异常（除了 `exceptionsToIgnore` 里面排除掉的异常类型）进行处理。若同时配置了 fallback 和 defaultFallback，则只有 fallback 会生效。defaultFallback 函数签名要求：
  - 返回值类型必须与原函数返回值类型一致；
  - 方法参数列表需要为空，或者可以额外多一个 `Throwable` 类型的参数用于接收对应的异常。
  - defaultFallback 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 `fallbackClass` 为对应的类的 `Class` 对象，注意对应的函数必需为 static 函数，否则无法解析。
- `exceptionsToIgnore`（since 1.6.0）：用于指定哪些异常被排除掉，不会计入异常统计中，也不会进入 fallback 逻辑中，而是会原样抛出。

> 注：1.6.0 之前的版本 fallback 函数只针对降级异常（`DegradeException`）进行处理，**不能针对业务异常进行处理**。

特别地，若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 `BlockException` 时只会进入 `blockHandler` 处理逻辑。若未配置 `blockHandler`、`fallback` 和 `defaultFallback`，则被限流降级时会将 `BlockException` **直接抛出**。

## 13 Sentinel实现服务熔断

- ![xLRQmt](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/xLRQmt.png)
  
  - 新建9003/9004作为服务端模块。
    
    ```java
        @GetMapping("/paymentSQL/{id}")
        public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id){
            Payment payment = hashMap.get(id);
            return new CommonResult<>(200, "from mysql,serverPort:" + serverPort, payment);
        }
    ```
  
  - 新建消费者模块84，使用Ribbon做负载均衡。
    
    - 在主配置文件中添加feign的支持:
      
      ```yml
      #激活sentinel对feign的支持
      feign:
        sentinel:
          enabled: true
      ```
    
    - 主启动类标注@EnableFeignClients //启用feign支持
    
    - 服务层通过Feign调用9003和9004提供的服务：
    
    ```java
        @RequestMapping("/consumer/fallback/{id}")
        @SentinelResource(value = "fallback") //没有配置,给客户Error页面，不友好
     public CommonResult<Payment> fallback(@PathVariable("id") Long id){
            CommonResult<Payment> commonResult = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class);
            if(id == 4){
                throw new IllegalArgumentException("IllegalArgumentException,非法参数异常");
            }else if(commonResult.getData() == null){
                throw new NullPointerException("NullPointerException,该ID没有记录，空指针异常");
            }
            return commonResult;
        }
    ```
  
  - 启动9003/9004/84服务，访问 http://127.0.0.1:84/consumer/fallback/1 ， 默认采用Ribbon的轮询负载均衡，显示的端口在9003和9004之间切换。
    
    - 访问 http://127.0.0.1:84/consumer/fallback/4 报错: 没有任何配置，直接返回Error页面，很不友好![3XxUXQ](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/3XxUXQ.png)

### 13.1 只配置fallback的情况

- 使用`@SentinelResource(value = "fallback",fallback = "handlerFallback")` 只配置fallback，fallback只负责业务异常：
  
  ```java
      // 本例是fallback
      public CommonResult handlerFallback(Long id, Throwable e){
          Payment payment = new Payment(id, null);
          return new CommonResult(444, "兜底异常handler，exception内容"+e.getMessage(), payment);
      }
  ```
  
  - 再次访问 http://127.0.0.1:84/consumer/fallback/4 ：![UNygCn](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/UNygCn.png)
  
  - 访问 http://127.0.0.1:84/consumer/fallback/5 ：![9mEEBW](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/9mEEBW.png)
  
  - 效果：**无论抛出什么异常都由 handlerFallback 来处理，fallback只负责业务异常**

### 13.2 只配置blockHandler的情况

- `@SentinelResource(value = "fallback", blockHandler = "blockHandler")`

- 配置了blockHandler，只负责sentinel控制台配置违规
  
  ```java
  public CommonResult blockHandler(Long id, BlockException exception){
         Payment payment = new Payment(id, null);
         return new CommonResult<>(445, "blockHandler-sentinel 限流，无此流水号：blockException" + exception.getMessage(), payment);
  }
  ```

- 访问http://127.0.0.1:84/consumer/fallback/4 ：照样进入Error页面：![kBhZHS](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/kBhZHS.png)

- 在Sentinel后台配置任意的流控规则，例如异常数流控：
  
  - 如果没有触发Sentienl的异常数降级，则还是显示Error页面
  
  - 如果触发了Sentinel的异常降级，则调用Handler方法，显示：![cDIgkf](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/cDIgkf.png)

- 总结：handler只负责sentinel流控或者降级的提示

### 13.3 fallbcak和blockHandler都配置的情况

- `@SentinelResource(value = "fallback",fallback = "handlerFallback", blockHandler = "blockHandler",  
   exceptionsToIgnore = {IllegalArgumentException.class}) // 配置了blockHandler和fallback`

- 效果：被限流/降级时调用blockHandler方法，java出现异常时调用fallback方法

- 配置异常例外项：
  
  - 对于一些比较严重的异常，我们还是选择直接返回Java的Error页面，而不是作出友好提示
  
  - `@SentinelResource(value = "fallback",fallback = "handlerFallback", blockHandler = "blockHandler", exceptionsToIgnore = {IllegalArgumentException.class})`
  
  - exceptionsToIgnore = {IllegalArgumentException.class} 的含义是忽略IllegalArgumentException，若出现IllegalArgumentException则不再调用fallbcak方法，而是直接返回Java的Error页面

### 13.4 Sentinel整合Ribbon + openFeign + fallback整合

- 引入OpenFeign的POM依赖

- 在84的主配置文件中开启Feign的支持，在主启动类中标注@EnableFeignClients

- 添加Feign服务接口，与生产者对应:
  
  ```java
  // Feign使用接口 + 注解的方式实现远程调用
  // fallback指定服务降级时调用的类
  @FeignClient(value = "nacos-payment-provider", fallback = PaymentFallbackService.class)
  public interface PaymentService {
  
      @GetMapping("/paymentSQL/{id}")
      CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
  }
  ```

- 编写`PaymentFallbackService`类指定服务降级时调用的类:

```java
  @Component
  public class PaymentFallbackService implements PaymentService {
      @Override
      public CommonResult<Payment> paymentSQL(Long id) {
          return new CommonResult<>(444, "服务降级 ---- fallback");
      }
  }
```

```
- 在控制器中增加方法：

```java
    @Resource
    private PaymentService paymentService;

    @GetMapping("/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id){
        return paymentService.paymentSQL(id);
    }
```

- 访问 http://127.0.0.1:84/consumer/paymentSQL/1 ： ![OFAmW2](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/OFAmW2.png)

- 关闭服务提供者9003/9004，生产者宕机，访问 http://127.0.0.1:84/consumer/paymentSQL/1 ，触发OpenFeign的服务降级:![SlN9Xj](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/SlN9Xj.png)

## 14 Sentinel 与 Hystrix、resilience4j 的对比

- [Guideline: 从 Hystrix 迁移到 Sentinel · alibaba/Sentinel Wiki](https://github.com/alibaba/Sentinel/wiki/Guideline:-%E4%BB%8E-Hystrix-%E8%BF%81%E7%A7%BB%E5%88%B0-Sentinel)

![kSh3sS](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/kSh3sS.png)

## 15 Sentinel规则持久化

- [Sentinel 实战-规则持久化 - 掘金](https://juejin.im/post/5c360bd2f265da616d546072)

- Sentinel的规则在重启后会消失，生产环境中需要持久化
  
  - 我们可以将Sentinel的规则持久化到Nacos中：
    
    - 例如，我们想持久化针对8401服务模块的Sentinel规则
    
    - 1、修改8401的POM文件，引入`sentinel-datasource-nacos`做持久化
    
    - 2、修改8401的主配置文件：
    
    ```yml
    server:
      port: 8401
    
    spring:
      application:
        name: cloudalibaba-sentinel-service
      cloud:
        nacos:
          discovery:
            server-addr: 127.0.0.1:8848
        sentinel:
          transport:
            dashboard: 127.0.0.1:8080
            # 默认为8719，如果被占用会自动+1，直到找到为止
            port: 8719
          # 流控规则持久化到nacos
          datasource:
            dsl:
              nacos:
                server-addr: 127.0.0.1:8848
                # 下面是在Nacos中对应的配置信息
                data-id: ${spring.application.name}
                group-id: DEFAULT_GROUP
                data-type: json
                rule-type: flow
    
    management:
      endpoints:
        web:
          exposure:
            include: "*"
    ```
    
    - 3、在Nacos中新建与8401主配置文件中对应的配置信息：![eR42qE](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/eR42qE.png)
      
      JSON配置文件内容中粘贴下面的信息：
      
      ![fMs1ye](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/fMs1ye.png)
    
    - 4、重启8401，Sentinel可以从Nacos中读取流控规则：![pI4SC9](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/pI4SC9.png)
    
    - 5、访问 http://127.0.0.1:8401/testB 流控规则有效

## 16 使用Seata处理分布式事务

- **事务**是指由一组操作组成的一个工作单元，这个工作单元具有原子性（atomicity）、一致性（consistency）、隔离性（isolation）和持久性（durability）。【ACID】
  
  - **原子性**：执行单元中的操作要么全部执行成功，要么全部失败。如果有一部分成功一部分失败那么成功的操作要全部回滚到执行前的状态。
  
  - **一致性**：执行一次事务会使用数据从一个正确的状态转换到另一个正确的状态，执行前后数据都是完整的。 
  
  - **隔离性**：在该事务执行的过程中，任何数据的改变只存在于该事务之中，对外界没有影响，事务与事务之间是完全的隔离的。只有事务提交后其它事务才可以查询到最新的数据。
  
  - **持久性**：事务完成后对数据的改变会永久性的存储起来，即使发生断电宕机数据依然在。

- **本地事务**就是用关系数据库来控制事务，关系数据库通常都具有ACID特性，传统的单体应用通常会将数据全部存储在一个数据库中，会借助关系数据库来完成事务控制。

- **分布式事务**就是指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上。简单的说，就是一次大的操作由不同的小操作组成，这些小的操作分布在不同的服务器上，且属于不同的应用，分布式事务需要保证这些小操作要么全部成功，要么全部失败。本质上来说，分布式事务就是为了保证不同数据库的数据一致性。

- BASE: 是 Basically Available(基本可用)、Soft state(软状态)和 Eventually consistent (最终一致性)三个短语的缩写
  
  - 基本可用:分布式系统在出现故障时，允许损失部分可用功能，保证核心功能可用。
  
  - 软状态:允许系统中存在中间状态，这个状态不影响系统可用性，这里指的是CAP中的不一致。
  
  - 最终一致:最终一致是指经过一段时间后，所有节点数据都将会达到一致。

- BASE解决了CAP中理论没有网络延迟，在BASE中用软状态和最终一致，保证了延迟后的一致性。BASE和 ACID 是相反的，它完全不同于ACID的强一致性模型，而是通过牺牲强一致性来获得可用性，并允许数据在一段时间内是不一致的，但最终达到一致状态。

### 16.1 分布式事务概述

[Java微服务下的分布式事务介绍及其解决方案_Java_oldshaui的博客-CSDN博客](https://blog.csdn.net/oldshaui/article/details/88743085)

[再有人问你分布式事务，把这篇扔给他 - 掘金](https://juejin.im/post/5b5a0bf9f265da0f6523913b)

- 多数据库、多数据源环境下的事务处理

- 例如：用户购买商品的业务。整个业务由3个微服务提供支持：
  
  - 仓储服务：扣除商品的库存数量
  
  - 订单服务：根据采购需求创建订单
  
  - 账户服务：从账户中扣除余额

- ![I86euH](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/I86euH.png)

### 16.2 Seata概述

- [Seata 是什么](https://seata.io/zh-cn/docs/overview/what-is-seata.html)

- Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。

- **Transcation ID(XID)**:全局唯一的事务id

- **TC - 事务协调者**：维护全局和分支事务的状态，驱动全局事务`提交`或`回滚`。

- **TM - 事务管理器**：定义全局事务的范围：开始全局事务、提交或回滚全局事务。

- **RM - 资源管理器**：管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。【可以理解为RM是一个个的数据库】

- 处理过程：![Z785MM](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/Z785MM.png)
  
  - 1.TM向TC申请开启一个全局事务,全局事务创建成功并生成一个全局唯一的XD
  
  - 2.Ⅺ1D在微服务调用链路的上下文中传播;
  
  - 3.RM向TC注册分支事务,将其纳入XD对应全局事务的管辖;
  
  - 4.TM向TC发起针对XID的全局提交或回滚决议;
  
  - 5.TC调度ⅪD下管辖的全部分支事务完成提交或回滚请求

### 16.3 Seata的安装与配置

[Seata 参数配置](http://seata.io/zh-cn/docs/user/configurations.html)

- 修改/conf文件夹下的file.conf文件，设置日志的存储：
  
  - 自定义事务组名称 + 事务日志存储模式设置为db + 数据库连接信息
  
  - 创建数据库seata，并运行[seata/script/server/db at develop · seata/seata · GitHub](https://github.com/seata/seata/tree/develop/script/server/db) 中的store.sql创建数据表

- 修改/conf目录下的registry文件，将配置注册进Nacos做持久化

- 启动时先启动Nacos，再启动Seata

### 16.4 订单/库存/账户业务数据库准备

- 需要三个微服务，一个订单服务，一个库存服务，一个账户服务 【下订单—> 减库存 -> 减余额 -> 改订单状态】

- 当用户下单时，会在订单服务中创建一个订单，然后通过远程调用库存服务来减少商品的库存，再通过远程调用账户服务来减少用户账户的余额。最终在订单服务中标记订单状态为已完成。

- 显然，需要有三个数据库。存在2次远程调用，明显会有分布式事务的问题。
  
  - `seata_order` 存储订单的数据库
  
  ```sql
  CREATE TABLE `t_order` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT(11) DEFAULT NULL,
      `product_id` BIGINT(11) DEFAULT NULL,
    `count` int(11) DEFAULT NULL,
      `money` DECIMAL(11,0) DEFAULT NULL,
      `status` INT(1) DEFAULT NULL COMMENT '订单状态，0表示创建中，1表示已经完成'
  ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
  ```
  
  - `seata_storage`存储库存的数据库
  
  ```sql
  CREATE TABLE `t_storage` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
      `product_id` BIGINT(11) DEFAULT NULL,
      `total` int(11) DEFAULT NULL,
    `used` int(11) DEFAULT NULL,
    `residue` int(11) DEFAULT NULL
  ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
  ```
  
  - `seata_account`存储账户信息的数据库
  
  ```sql
  CREATE TABLE `t_account` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
      `user_id` BIGINT(11) DEFAULT NULL,
      `total` DECIMAL(10,0) DEFAULT NULL,
    `used` DECIMAL(10,0) DEFAULT NULL,
    `residue` DECIMAL(10,0) DEFAULT '0'
  ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
  ```

- 在三个数据库中都建立回滚日志表，都执行一次/seata/conf目录下的`db_undo_log.sql`

### 16.5 订单微服务order-module 2001的编写

- 1、引入POM

- 2、修改主配置文件

- 3、复制seata目录下的file.conf到/resources目录下。注意修改服务组名、MySQL数据库连接信息

- 5、复制seata目录下的file.conf到/resources目录下。注意修改Nacos配置

- 6、编写Domain实体类与数据库表对应

- 7、编写dao.OrderDao接口实现MyBatis操作数据库。一共两个方法，create方法新建订单，update方法更新订单状态【是否已经完成】

- 8、编写OrderMapper.xml配置接口对应的SQL

- 9、编写2个Service接口StorageService和AccountService用于Feign远程调用生产者提供的服务

- 10、编写OrderService接口和OrderServiceIml实现类

- 11、编写Controller，暴露 /order/create 来创建订单

- 12、编写`MyBatisConfig`完成Mybaits相关配置，使用seata管理数据源(使用seata对数据源进行代理)

- 13、编写主启动类`@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //取消数据源自动创造`

- 14、先启动Nacos和Seata，再启动2001微服务

### 16.6 库存微服务storage-module 2002的编写

- 大部分操作和2001一样

### 16.7 库存微服务account-module的编写

- 大部分操作和2001一样

### 16.8不使用Seata的@GlobalTransactional注解时存在的问题

- 准备好环境：空的t_order表，存在库存记录和账户记录的t_storage和t_accout

- （一）测试在**没有加@GlobalTransactional，并且没有超时异常**的情况：
  
  - ![QG9dUS](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/QG9dUS.png)
  
  - 在没有发生异常的情况下，不会出现事务问题

- （二）测试在**没有加@GlobalTransactional，发生超时异常**的情况：
  
  - 在`seata-account-service2003`的`AccountServiceImpl`方法中sleep20秒，由于Feign默认等待时间是1s，所以肯定会报超时异常
  
  - ![](/Users/brightzh/Library/Application%20Support/marktext/images/2020-05-07-00-30-34-image.png)
  
  - ![ac2RKt](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/ac2RKt.png)
  
  - ![RfiWox](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/RfiWox.png)
  
  - ![pt3OG5](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/pt3OG5.png)
  
  - 分析：调用库存模块正常，库存正常减少。账户模块虽然睡了20s，但是之后依然会完成扣款操作。而左右调用方的2001模块由于Feign的超时机制没有拿到返回结果，因此没能完成订单，显示订单状态为0。

### 16.9 使用Seata的@GlobalTransactional解决分布式事务问题

- 依旧保留超时异常问题，但是在`OrderServiceImpl`中添加@GlobalTransactional

- `@GlobalTransactional(name = "cloud-create-order", rollbackFor = Exception.class) // 发生任何异常都回滚`
  
  - name属性可以随便取名字

- ![](/Users/brightzh/Library/Application%20Support/marktext/images/2020-05-07-00-43-20-image.png)

- 刷新数据库之后发现事务已经回滚

### 16.10 Seata原理简介

- ![](/Users/brightzh/Library/Application%20Support/marktext/images/2020-05-07-11-47-34-image.png)
  
  - **Transcation ID(XID)**:全局唯一的事务id
  
  - **RM - 资源管理器**：管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。【可以理解为RM是一个个的数据库】
  
  - **TC - 事务协调者**：维护全局和分支事务的状态，驱动全局事务`提交`或`回滚`。
  
  - **TM - 事务管理器**：定义全局事务的范围：开始全局事务、提交或回滚全局事务

- 【Seata AT 模式】执行流程：
  
  - TM开启分布式事务（TM向TC注册全局事务记录）
  
  - 按业务场景，编排数据库、服务等事务内资源（RM向TC汇报资源准备状态）
  
  - TM结束分布式事务，事务一阶段结束（TM通知TC提交/回滚分布式事务）
  
  - TC汇总事务信息，决定分布式事务是提交还是回滚
  
  - TC通知所有RM 提交/回滚，事务二阶段结束

- 【一阶段】：业务数据和回滚日志记录在同一个本地事务中提交，释放本地锁和连接资源。
  
  - 在一阶段, Seata会拦截“业务SQL
    
    ![NOa3UM](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/NOa3UM.png)
    
    - 1、解析SQL语义,找到“业务SQL"要更新的业务数据,在业务数据被更新前,将其保存成" **before image**"（前置镜像）
    
    - 2、执行“业务SQL”更新业务数据,在业务数据更新之后,
    
    - 3、其保存成" **after image**”,最后生成行锁。
    
    - 以上操作全部在一个数据库事务内完成,这样保证了一阶段操作的原子性。

- 【二阶段】：
  
  - 提交异步化，非常快速地完成。
  - 回滚通过一阶段的回滚日志进行反向补偿
  - 1、二阶段如过顺利提交的话,因为业务SQL在一阶段已经提交至数据库,所以 Seata框架只需将一阶段保存的快照数据和行锁删掉,完成数据清理即可
    - ![Gi2NP7](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/Gi2NP7.png)
  - 2、二阶段如果发生异常
    - 二阶段如果是回滚的话, Seata就需要回滚一阶段已经执行的"业务SQL",还原业务数据
    - 回滚方式便是用" before image”还原业务数据;但在还原前要首先要校验脏写,对比“数据库当前业务数据”和 "after image"
    - 如果两份数据完全一致就说明没有脏写,可以还原业务数据,如果不—致就说明有脏写,出现脏写就需要转人工处理。
    - ![lq6db0](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/05/lq6db0.png)

- 





## 参考资料

- [spring-cloud-alibaba/README-zh.md at master · alibaba/spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)

- [说说我为什么看好Spring Cloud Alibaba - 掘金](https://juejin.im/post/5c9d78715188251e3e3c8a7f)

- [Spring Cloud Alibaba与Spring Boot、Spring Cloud之间不得不说的版本关系 | 程序猿DD](http://blog.didispace.com/spring-cloud-alibaba-version/)

- https://github.com/alibaba/spring-cloud-alibaba/wiki

- [Nacos](https://nacos.io/zh-cn/docs/what-is-nacos.html)

- [alibaba/nacos: an easy-to-use dynamic service discovery, configuration and service management platform for building cloud native applications.](https://github.com/alibaba/nacos)

- [14. SpringCloud Alibaba Nacos服务注册和配置中心 - 掘金](https://juejin.im/post/5e74400ee51d4526cd1e0cb6#heading-35)

- [Nacos集群部署说明](https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html)

- [Nginx for Mac install、start、stop 、reload实践 - 知乎](https://zhuanlan.zhihu.com/p/38485095)

- [nginx 命令笔记(mac) - 简书](https://www.jianshu.com/p/36bc3728cb44)

- [搭建生产可用的Nacos集群 | 周立的博客 - 关注Spring Cloud、Docker](http://www.itmuch.com/spring-cloud-alibaba/nacos-ha/)

- [介绍 · alibaba/Sentinel Wiki](https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D)

- [sentinel](https://sentinelguard.io/zh-cn/)

- [Sentinel 与 Hystrix 的对比-云栖社区-阿里云](https://yq.aliyun.com/articles/623424)

- [Sentinel introduction](https://sentinelguard.io/zh-cn/docs/introduction.html)

- [用 Postman 做接口自动化（六）Collection Runner 运行参数设置_网络_小满测试-CSDN博客](https://blog.csdn.net/minzhung/article/details/102502266)

- [【Postman】10 Postman Runner的使用 - 知乎](https://zhuanlan.zhihu.com/p/109078237)

- [Guideline: 从 Hystrix 迁移到 Sentinel · alibaba/Sentinel Wiki](https://github.com/alibaba/Sentinel/wiki/Guideline:-%E4%BB%8E-Hystrix-%E8%BF%81%E7%A7%BB%E5%88%B0-Sentinel)

- [Sentinel 实战-规则持久化 - 掘金](https://juejin.im/post/5c360bd2f265da616d546072)

- [seata/seata: Seata is an easy-to-use, high-performance, open source distributed transaction solution.](https://github.com/seata/seata)

- [Java微服务下的分布式事务介绍及其解决方案_Java_oldshaui的博客-CSDN博客](https://blog.csdn.net/oldshaui/article/details/88743085)

- [再有人问你分布式事务，把这篇扔给他 - 掘金](https://juejin.im/post/5b5a0bf9f265da0f6523913b)

- [Seata 中文官网](https://seata.io/zh-cn/)

- [Seata 中文文档](https://seata.io/zh-cn/docs/overview/what-is-seata.html)

- [Seata AT 模式](https://seata.io/zh-cn/docs/dev/mode/at-mode.html)

- [分布式事务中间件Seata的设计原理](http://objcoding.com/2019/07/11/seata/)

- 
