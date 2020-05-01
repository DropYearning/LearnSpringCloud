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

















## 参考资料

- [spring-cloud-alibaba/README-zh.md at master · alibaba/spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)

- [说说我为什么看好Spring Cloud Alibaba - 掘金](https://juejin.im/post/5c9d78715188251e3e3c8a7f)

- [Spring Cloud Alibaba与Spring Boot、Spring Cloud之间不得不说的版本关系 | 程序猿DD](http://blog.didispace.com/spring-cloud-alibaba-version/)

- https://github.com/alibaba/spring-cloud-alibaba/wiki

- [Nacos](https://nacos.io/zh-cn/docs/what-is-nacos.html)

- [alibaba/nacos: an easy-to-use dynamic service discovery, configuration and service management platform for building cloud native applications.](https://github.com/alibaba/nacos)

- 
