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

### 5.1 配置Nacos支持mysql

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
  ```
  
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
  - 以后启动Nacos实例时使用`./shartup.sh -p 3333`就可以运行一个3333端口的Nacos

- 配置Nginx:

  ```nginx

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

      upstream cluster{
          server 127.0.0.1:3333;
          server 127.0.0.1:4444;
          server 127.0.0.1:5555;
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
  
  - 

## 参考资料

- [spring-cloud-alibaba/README-zh.md at master · alibaba/spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)

- [说说我为什么看好Spring Cloud Alibaba - 掘金](https://juejin.im/post/5c9d78715188251e3e3c8a7f)

- [Spring Cloud Alibaba与Spring Boot、Spring Cloud之间不得不说的版本关系 | 程序猿DD](http://blog.didispace.com/spring-cloud-alibaba-version/)

- https://github.com/alibaba/spring-cloud-alibaba/wiki

- [Nacos](https://nacos.io/zh-cn/docs/what-is-nacos.html)

- [alibaba/nacos: an easy-to-use dynamic service discovery, configuration and service management platform for building cloud native applications.](https://github.com/alibaba/nacos)

- [Nacos集群部署说明](https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html)

- [Nginx for Mac install、start、stop 、reload实践 - 知乎](https://zhuanlan.zhihu.com/p/38485095)

- [nginx 命令笔记(mac) - 简书](https://www.jianshu.com/p/36bc3728cb44)

- [搭建生产可用的Nacos集群 | 周立的博客 - 关注Spring Cloud、Docker](http://www.itmuch.com/spring-cloud-alibaba/nacos-ha/)
