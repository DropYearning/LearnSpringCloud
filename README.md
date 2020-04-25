LearnSpringCloud

SpringBoot 2.x + SpringCloud **Hoxton**

[尚硅谷2020 SpringCloud(H版&alibaba)框架开发教程_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV18E411x7eT?from=search&seid=12947583133329416541)



## 0 环境版本说明

- SpringCloud与SpringBoot版本的对应：
    - ![3nRwCM](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/3nRwCM.png)

- 本系列工程的版本要求：
    - SpringCloud Hoxton.SR1
    - SpringBoot 2.2.2.RELEASE
    - cloud alibaba 2.1.0.RELEASE
    - java java8
    - Maven 3.5+
    - MySQL 5.7+
- **SpringCloud组件的更替**
    - <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/X9KL7B.jpg" alt="X9KL7B" style="zoom:150%;" />

## 1 SpringCloud与微服务

- *SpringCloud*是一套完整的分布式微服务解决方案,基于SpringBoot框架。
- ![qOO4M3](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/qOO4M3.png)




## 附录：订单-支付系统微服务的实现

### 1  创建父工程

- 开启idea注解支持（主要是为了兼容Lombok）：![MbAIsJ](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/MbAIsJ.png)

- 设置`<packaging>pom</packaging>` : 意思是使用maven分模块管理，都会有一个父级项目，pom文件一个重要的属性就是packaging（打包类型），**一般来说所有的父级项目的packaging都为pom**

- 父工程只余下`pom.xml`即可

- `<dependencyManagement>` 子模块继承之后，提供：锁定版本, 并且子模块不用再写group和version。**Maven会沿着父子层次向上走，直到找到一个拥有`<dependencyManagement>` 元素的项目，然后它就会使用这个`<dependencyManagement>` 元素中指定的版本号**

    - 这样做的好处是，如果有多个子项目都引用一样的依赖，则可以避免在每一个子项目中都声明一个版本号。这样当需要更新jar包版本的时候只需要在父项目中修改，而不需要一个个在子项目中修改。如果子项目需要使用另外的版本，只需要在子项目中再声明version即可
    - `<dependencyManagement>`只声明版本，不引入依赖，引入依赖的标签是`<dependencies>`

- 配置idea热部署生效：![w03Nxh](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/w03Nxh.png)

    ![Bv8XOv](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/Bv8XOv.png)

- maven中跳过单元测试：![wUONhS](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/wUONhS.png)

- 父工程创建完毕后执行`mvn:install`将父工程发布到仓库中方便子工程继承

    ```xml
    <!--统一管理jar包和版本-->
      <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.16.18</lombok.version>
        <mysql.version>8.0.19</mysql.version>
        <druid.verison>1.1.16</druid.verison>
        <mybatis.spring.boot.verison>1.3.0</mybatis.spring.boot.verison>
      </properties>
    
      <!--子模块继承之后，提供：锁定版本+ 子模块不用再写  group和version-->
      <dependencyManagement>
        <dependencies>
          <!--spring boot 2.2.2-->
          <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.2.2.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
          </dependency>
          <!--spring cloud Hoxton.SR1-->
          <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Hoxton.SR1</version>
            <type>pom</type>
            <scope>import</scope>
          </dependency>
          <!--spring cloud alibaba 2.1.0.RELEASE-->
          <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.2.0.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
          </dependency>
          <!-- MySql -->
          <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
          </dependency>
          <!-- Druid -->
          <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>${druid.verison}</version>
          </dependency>
          <!-- mybatis-springboot整合 -->
          <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.spring.boot.verison}</version>
          </dependency>
          <!--lombok-->
          <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
          </dependency>
          <!--junit-->
          <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
          </dependency>
          <!-- log4j -->
          <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>${log4j.version}</version>
          </dependency>
        </dependencies>
      </dependencyManagement>
    
      <build>
        <plugins>
          <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
              <fork>true</fork>
              <addResources>true</addResources>
            </configuration>
          </plugin>
        </plugins>
      </build>
    ```

    

### 2 cloud-provider-payment8001微服务提供者支付模块

- 1、建module

- 2、改POM依赖`pom.xml`

- 3、写YAML配置文件`application.yaml`

- 4、写主启动类

    ```java
    @SpringBootApplication
    public class PaymentMain8001 {
        public static void main(String[] args) {
            SpringApplication.run(PaymentMain8001.class, args);
        }
    }
    ```

- 5、业务类的实现：
    - 1、建表
    - 2、编写实体类
    - 3、JSON封装体CommonResult
    - 4、编写dao和对应的dao.xml 配置文件
    - 5、再编写Service\Controller
        - [@RequestBody的使用_Java_JustryDeng-CSDN博客](https://blog.csdn.net/justry_deng/article/details/80972817)
    - 6、测试业务类

### 3 cloud-consumer-order801微服务消费者订单模块（RestTemplate）

- 按照以前的步骤编写微服务

- Controller中使用**restTemplate**调用生产者模块的rest服务

    - 是spring提供的用于访问Rest服务的客户端模板工具集[RestTemplate (Spring Framework 5.2.5.RELEASE API)](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html)

    - [SpringBoot系列 - 使用RestTemplate | 飞污熊博客](https://www.xncoding.com/2017/07/06/spring/sb-restclient.html)

    - 使用前需要写写入到Spring的配置文件or配置类中：

        ```java
        // com.example.springcloud.config.ApplicationContextConfig
        @Configuration
        public class ApplicationContextConfig {
        
            @Bean
            public RestTemplate getRestTemplate(){
                return new RestTemplate();
            }
            
        }
        
        ```

### 8.4 工程重构（抽取公共类）

- <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/Y3BASA.png" alt="Y3BASA" style="zoom:67%;" />
- 两个微服务模块中都有重复的entitis实体类，可以将其抽出：
    - 1、新建一个公共模块:cloud-api-commons
    - 2、修改POM
    - 3、添加公共类
    - 4、使用Maven工具`mvn:install`将该模块打包成jar放入仓库供其他模块引用
    - 5、在其他模块中删除公共部分。在它们各自的POM文件中引入公共的cloud-api-commons.jar
    - 6、测试原有的模块是否正常
    - 好处：以后如果需要修改公共部分只需要修改一处

### 8.5  注册中心与Eureka

- **使用注册中心必要性**：之前的订单-支付系统是使用restTemplate相互调用的。
    - 在云计算之前，服务部署在物理机器上，IP 地址不变，因此即使没有服务发现，通过 **硬编码** 服务的地址，也能满足需求。
    - 云计算时代，尤其是 Docker 的快速发展，使得 **硬编码** 几乎无用武之地，因为服务不再部署在物理机上，每次新创建的实例，其 IP 很可能与上次不同，因此需要更加灵活的服务发现机制。
    - 在分布式系统中，我们不仅仅是需要在注册中心找到服务和服务地址的映射关系这么简单，我们还需要考虑更多更复杂的问题：服务注册后，如何被及时发现；服务宕机后，如何及时下线；服务如何有效的水平扩展；服务发现时，如何进行路由；服务异常时，如何进行降级注册中心如何实现自身的高可用
- 服务中心又称注册中心，管理各种服务功能包括**服务的注册、发现、熔断、负载、降级**等，比如dubbo admin后台的各种功能。
- [springcloud(二)：注册中心Eureka - 纯洁的微笑 - 博客园](https://www.cnblogs.com/ityouknow/p/6854805.html)

#### 8.5.1 Eureka基础知识

- Spring Cloud 封装了 Netflix 公司开发的 Eureka 模块来实现服务注册和发现。**Eureka 采用了 C-S 的设计架构。**Eureka Server 作为服务注册功能的服务器，它是服务注册中心。而系统中的其他微服务，使用 Eureka 的客户端连接到 Eureka Server，并维持心跳连接。这样系统的维护人员就可以通过 Eureka Server 来监控系统中各个微服务是否正常运行。Spring Cloud 的一些其他模块（比如Zuul）就可以通过 Eureka Server 来发现系统中的其他微服务，并执行相关的逻辑。
- **Eureka由两个组件组成：Eureka服务器和Eureka客户端**。Eureka服务器用作服务注册服务器。Eureka客户端是一个java客户端，用来简化与服务器的交互、作为轮询负载均衡器，并提供服务的故障切换支持。Netflix在其生产环境中使用的是另外的客户端，它提供基于流量、资源利用率以及出错状态的加权负载均衡。
    - ![nbneJ9](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/nbneJ9.jpg)
    - **Eureka Server**：提供服务注册和发现
    - **Service Provider**：服务提供方，将自身服务注册到Eureka，从而使服务消费方能够找到
    - **Service Consumer**：服务消费方，从Eureka获取注册服务列表，从而能够消费服务

#### 8.5.2 Eureka单机部署

- （一）新建模块`cloud-eureka-server-7001`

    - 1、修改POM。引入eureka依赖

        ```xml
          <!--eureka-server-->
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
                </dependency>
        ```

    - 2、在application.yml中对eureka进行设置

        ```yaml
        # 微服务端口
        server:
          port: 7001
        # 配置eureka
        eureka:
          instance:
            # 主机域名
            hostname: localhost
          client:
            # 设置服务中心不将自己注册进去
            register-with-eureka: false
            # 服务中心不需要检索
            fetch-registry: false
            service-url:
              # 设置与Eureka Server交互的地址
                defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
        
        ```

    - 3、在主启动类上标注`@EnableEurekaServer`表示这是一个Eureka注册中心

    - 4、访问http://127.0.0.1:7001/ 查看是否成功：![j6lQ2h](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/j6lQ2h.png)

- （二）将`cloud-provider-payment8001`注册在服务中心中：

    - 1、在`cloud-provider-payment8001`的POM中引入Eureka的依赖：（2.x 后Eureka的jar包区分client和server）

        ```xml
         <!--引入Eureka client-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            </dependency>
        ```

    - 2、在主配置中增加Eureka相关的项：

        ```
        # 配置Eureka
        eureka:
          client:
            # 表示是否将自己注册进EurekaServer，默认为true
            register-with-eureka: true
            # 是否从EurekaServer抓取已有的注册信息，默认为true
            fetch-registry: true
            service-url:
              # 单机版
              defaultZone: http://localhost:7001/eureka
        ```

    - 3、在8001的主启动类上标注`@EnableEurekaClient`
    - ![XMto8r](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/XMto8r.png)

- （三）将`cloud-consumer-order801`注册在服务中心中：

    - 与上面过程类似
    - ![538q1Y](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/538q1Y.png)

#### 8.5.3 Eureka集群部署

[微服务注册中心Eureka架构深入解读 - InfoQ](https://www.infoq.cn/article/jlDJQ*3wtN2PcqTDyokh)

- **集群原理说明**：

    - ![2em1DS](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/2em1DS.jpg)
    - <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/mwXkc1.png" alt="mwXkc1" style="zoom:150%;" />
    - 微服务RPC远程调用最核心的是**高可用**，一旦唯一的服务中心宕机，会导致整个系统的服务都不可用。因此实际项目中往往需要部署多个注册中心，实现故障容错和负载均衡
    - **互相注册，相互守望，对外暴露成一个整体**：![HakTt0](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/HakTt0.png)

- 本地Eureka集群的部署步骤：

    - 1、照抄单机版部署过程，增加一个新的服务中心模块`cloud-eureka-7002`

    - 2、修改映射配置文件 host

        ```
        # SpringCloud
        127.0.0.1 eureka7001.com
        127.0.0.1 eureka7002.com
        ```

    - 3、修改`cloud-eureka-7002`和`cloud-eureka-7001`中的主配置文件

        ```yaml
        # 微服务端口
        server:
          port: 7002
        
        # 配置eureka
        eureka:
          instance:
            # 主机域名
            hostname: eureka7002.com
          client:
            # 设置服务中心不将自己注册进去
            register-with-eureka: false
            # 服务中心不需要检索
            fetch-registry: false
            service-url:
              # 单机版
              # defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
              # 集群版：在使用Eureka集群时此处写别的所有注册中心的服务地址
              defaultZone: http://eureka7001.com:7001/eureka/
        ```

    - 4、修改各个要注册进去的微服务模块的主配置文件：

        ```yaml
        # 微服务端口号
        server:
          port: 801
        # 配置微服务名称
        spring:
          application:
            name: cloud-order-service
        
        # 配置Eureka
        eureka:
          client:
            # 表示是否将自己注册进EurekaServer，默认为true
            register-with-eureka: true
            # 是否从EurekaServer抓取已有的注册信息，默认为true
            fetch-registry: true
            service-url:
              # 单机版
              # defaultZone: http://localhost:7001/eureka
              # 集群版
              defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
        ```

        

- 效果：

    - ![NDJZfS](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/NDJZfS.png)

    - 

- Eureka细节：主机名的修改和IP的显示

    ```yaml
      instance:
        instance-id: payment8001
        # 访问路径可以显示ip地址
        prefer-ip-address: true
    ```

    

#### 8.5.4 服务发现Discovery

- 对于注册进Eureka里面的微服务，可以通过服务发现来获得该服务的信息

- 在`cloud-provider-payment8001`的主启动类上标注@EnableDiscoveryClient

- 并在Controller类上增加如下方法：

    ```java
     @GetMapping(value = "/payment/discovery")
        public Object discovery(){
            
            // 获得Eureka中注册所有服务名
            List<String> services = discoveryClient.getServices();
            for (String service : services) {
                log.info("*****service:" + service);
            }
    
            // 获得Eureka中注册的CLOUD-PAYMENT-SERVICE服务的所有实例
            List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
            for (ServiceInstance instance : instances) {
                log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
            }
    
            return this.discoveryClient;
        }
    ```

- 可以在控制台看到输出的各种微服务的信息：

    ![npfLDY](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/npfLDY.png)

    ![dTYjZF](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/dTYjZF.png)

#### 8.5.5 Eureka的自我保护机制

- ![VQAbXN](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/VQAbXN.png)

    `EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.`

- **Eureka默认开启自我保护机制**

- **Eureka Server进入保护模式后不会再删除服务注册表中的数据，也就是不会注销任何服务。**【某时刻某一个微服务不可用了，Eureka不会立即清理，依旧会对该微服务信息进行保留】

    - [SpringCloud警告(Eureka)：EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE. - gudi - 博客园](https://www.cnblogs.com/gudi/p/8645370.html)
    - **默认情况下,如果 Eureka Server在一定时间内没有接收到某个微服务实例的心跳,Eureka将会注销该实例(默认90秒)**。但是当网络分区故障发生(延时、卡顿、拥挤)时,微服务与 EurekaServer之间无法正常通信,以上行为可能变得非常危险了——可能此时微服务本身其实是健康的,仅是因为EurekaClient到EurekaServer的网络出现了延迟,因此不应该立即注销这个微务。 Eureka通过“自我保护模式”来解决这个问题
    - **当 Eureka Server节点在短时间内丢失过多客户端时(可能发生了网络分区故障),那么这个节点就会进入自我保护模式。**
    -  它的设计哲学就是宁可保留暂时不健康的微服务实例，也不盲目注销可能健康的服务实例。

- 禁止自我保护（一掉线就注销）的方法：

    - 在`cloud-eureka-server-7001`中修改主配置文件：

        ```yaml
        # 配置eureka
        eureka:
          ...
          server:
            # 关闭自我保护机制
            enable-self-preservation: false
            # 修改心跳断连时间为2s
            eviction-interval-timer-in-ms: 2000
        ```

    - 修改`cloud-provider-payment8001`的主配置文件：

        ```yaml
        # 配置Eureka
        eureka:
        	...
          instance:
            instance-id: payment8001
            # 访问路径可以显示ip地址
            prefer-ip-address: true
            # Eureka客户端像服务端发送心跳的时间间隔，单位为秒（默认是30秒）
            lease-renewal-interval-in-seconds: 1
            # Eureka服务段在收到最后一次心跳后等待的时间上线，超出次上线后将删除服务（默认是90秒）
            lease-expiration-duration-in-seconds: 2
        ```

    - ![QsKj9H](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/QsKj9H.png)

### 8.6 支付模块cloud-provider-payment的集群部署

- 新建`cloud-provider-payment8002`

- 修改controller，使得查询结果页面显示当前是由哪一台服务器提供的查询服务：

    ```java
    @Value("${server.port}") // 可以读取配置文件中${server.port}的值， 目的是在结果页面显示本次查询由哪一台服务器提供
        private String serverPort;
        
    @GetMapping(value = "/payment/get/{id}") // 查询Get
        public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
            Payment result = paymentService.getPaymentById(id);
            log.info("*****查询结果:" + result);
            if (result != null ) {
                return new CommonResult(200, "serverPort:" + serverPort + "查询数据成功", result);
            }else {
                return new CommonResult(444, "serverPort:" + serverPort + "没有对应记录, 查询id:" + id, null);
            }
        }
    ```

- 修改消费者`cloud-consumer-order801`中调用服务的url，使之指向具体的服务名称：

    ```
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
    ```

- 在`getRestTemplate`中使用`@LoadBalanced`注解开启负载均衡

    > 只有开启了负载均衡后，才可以使用eureka中注册的服务名调用具体的服务。默认的负载均衡机制是**轮询**

- ![WCOLo9](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/WCOLo9.png)

> 实际开发中不应该复制多个模块，可以打包成jar，修改配置文件运行多个jar实例（多实例启动）

### 8.7 ZooKeeper代替Eureka充当注册中心

- [Eureka 2.0 开源工作宣告停止，继续使用风险自负 - OSCHINA](https://www.oschina.net/news/97521/eureka-2-0-discontinued)

- ZooKeeper是Hadoop的正式子项目，它是一个针对大型分布式系统的可靠协调系统，提供的功能包括：配置维护、名字服务、分布式同步、组服务等。ZooKeeper的目标就是封装好复杂易出错的关键服务，将简单易用的接口和性能高效、功能稳定的系统提供给用户。**ZooKeeper也可以实现注册中心的功能**

- 新增生产者模块`cloud-provider-payment8004`：

    - Docker部署ZooKeeper`docker run --name zookeeper -p 2181:2181 -d zookeeper`

        - [使用 Docker 一步搞定 ZooKeeper 集群的搭建 - 后台开发 - SegmentFault 思否](https://segmentfault.com/a/1190000006907443)
        - [Docker安装Zookeeper并进行操作_大数据_Radom&7-CSDN博客](https://blog.csdn.net/qq_26641781/article/details/80886831)
        - [IDEA 中安装和使用 zookeeper插件 - 直到世界的尽头-的个人空间 - OSCHINA](https://my.oschina.net/leitingweb/blog/617588)

    - 1、ZooKeeper主配置文件如下：

        ```yaml
        # 微服务端口号
        server:
          port: 8004
        
        spring:
          application:
            # 服务别名---注册zookeeper到注册中心的名称
            name: cloud-provider-payment
          cloud:
            zookeeper:
              # 默认localhost:2181
              connect-string: 192.168.99.114:2181
        ```

    - 2、修改主启动类：

        ```java
        @SpringBootApplication
        @EnableDiscoveryClient  // 该注解用于向consul或者zookeeper作为服务中心时注册服务
        public class PaymentMain8004 {
            public static void main(String[] args) {
                SpringApplication.run(PaymentMain8004.class, args);
            }
        }
        
        ```

    - 3、编写简单的Controller类

        ```java
        @RestController
        @Slf4j
        public class PaymentController {
        
            @Value("${server.port}") // 可以读取配置文件中${server.port}的值， 目的是在结果页面显示本次查询由哪一台服务器提供
            private String serverPort;
        
            @RequestMapping(value = "/payment/zk")
            public String paymentzk(){
                return "Spring Cloud with ZooKeeper:" + serverPort + "\t" + UUID.randomUUID().toString();
            }
        }
        ```

    - 







## 参考资料

- [1.5W 字搞懂 Spring Cloud，太牛了！](https://mp.weixin.qq.com/s/EHPKm50KmHq_KZIHyVef3A)

- [SpringCloud(H版)学习---服务注册中心 - coder、 - 博客园](https://www.cnblogs.com/rmxd/p/12547231.html)
- [espmihacker/cloud2020: 尚硅谷2020最新版SpringCloud(H版&alibaba)框架开发教程全套完整版从入门到精通(大牛讲授spring cloud)](https://github.com/espmihacker/cloud2020)
- [SpringMVC @ResponseBody和@RequestBody使用 - 简书](https://www.jianshu.com/p/7097fea8ce3f)
- [springcloud(二)：注册中心Eureka - 纯洁的微笑 - 博客园](https://www.cnblogs.com/ityouknow/p/6854805.html)
- [微服务注册中心Eureka架构深入解读 - InfoQ](https://www.infoq.cn/article/jlDJQ*3wtN2PcqTDyokh)
- [IDEA Services 工具窗口: 一个管理所有服务的地方【译】 - 简书](https://www.jianshu.com/p/2e2332f247fe)