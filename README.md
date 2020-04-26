LearnSpringCloud

SpringBoot 2.x + SpringCloud **Hoxton**

[2020 SpringCloud(H版&alibaba)框架开发教程_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV18E411x7eT?from=search&seid=12947583133329416541)



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

    > 理论上所有写在新建配置类中的配置项都可以直接写在主启动类中。

### 4 工程重构（抽取公共类）

- <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/Y3BASA.png" alt="Y3BASA" style="zoom:67%;" />
- 两个微服务模块中都有重复的entitis实体类，可以将其抽出：
    - 1、新建一个公共模块:cloud-api-commons
    - 2、修改POM
    - 3、添加公共类
    - 4、使用Maven工具`mvn:install`将该模块打包成jar放入仓库供其他模块引用
    - 5、在其他模块中删除公共部分。在它们各自的POM文件中引入公共的cloud-api-commons.jar
    - 6、测试原有的模块是否正常
    - 好处：以后如果需要修改公共部分只需要修改一处

### 5  注册中心与Eureka

- **使用注册中心必要性**：之前的订单-支付系统是使用restTemplate相互调用的。
    - 在云计算之前，服务部署在物理机器上，IP 地址不变，因此即使没有服务发现，通过 **硬编码** 服务的地址，也能满足需求。
    - 云计算时代，尤其是 Docker 的快速发展，使得 **硬编码** 几乎无用武之地，因为服务不再部署在物理机上，每次新创建的实例，其 IP 很可能与上次不同，因此需要更加灵活的服务发现机制。
    - 在分布式系统中，我们不仅仅是需要在注册中心找到服务和服务地址的映射关系这么简单，我们还需要考虑更多更复杂的问题：服务注册后，如何被及时发现；服务宕机后，如何及时下线；服务如何有效的水平扩展；服务发现时，如何进行路由；服务异常时，如何进行降级注册中心如何实现自身的高可用
- 服务中心又称注册中心，管理各种服务功能包括**服务的注册、发现、熔断、负载、降级**等，比如dubbo admin后台的各种功能。
- [springcloud(二)：注册中心Eureka - 纯洁的微笑 - 博客园](https://www.cnblogs.com/ityouknow/p/6854805.html)

#### 5.1 Eureka基础知识

- Spring Cloud 封装了 Netflix 公司开发的 Eureka 模块来实现服务注册和发现。**Eureka 采用了 C-S 的设计架构。**Eureka Server 作为服务注册功能的服务器，它是服务注册中心。而系统中的其他微服务，使用 Eureka 的客户端连接到 Eureka Server，并维持心跳连接。这样系统的维护人员就可以通过 Eureka Server 来监控系统中各个微服务是否正常运行。Spring Cloud 的一些其他模块（比如Zuul）就可以通过 Eureka Server 来发现系统中的其他微服务，并执行相关的逻辑。
- **Eureka由两个组件组成：Eureka服务器和Eureka客户端**。Eureka服务器用作服务注册服务器。Eureka客户端是一个java客户端，用来简化与服务器的交互、作为轮询负载均衡器，并提供服务的故障切换支持。Netflix在其生产环境中使用的是另外的客户端，它提供基于流量、资源利用率以及出错状态的加权负载均衡。
    - ![nbneJ9](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/nbneJ9.jpg)
    - **Eureka Server**：提供服务注册和发现
    - **Service Provider**：服务提供方，将自身服务注册到Eureka，从而使服务消费方能够找到
    - **Service Consumer**：服务消费方，从Eureka获取注册服务列表，从而能够消费服务

#### 5.2 Eureka单机部署

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

#### 5.3 Eureka集群部署

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

> 服务名不要有下划线_

#### 5.4 服务发现Discovery

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

#### 5.5 Eureka的自我保护机制

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

### 6 支付模块cloud-provider-payment的集群部署

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

### 7 ZooKeeper代替Eureka充当注册中心

- [Eureka 2.0 开源工作宣告停止，继续使用风险自负 - OSCHINA](https://www.oschina.net/news/97521/eureka-2-0-discontinued)

- ZooKeeper是Hadoop的正式子项目，它是一个针对大型分布式系统的可靠协调系统，提供的功能包括：配置维护、名字服务、分布式同步、组服务等。ZooKeeper的目标就是封装好复杂易出错的关键服务，将简单易用的接口和性能高效、功能稳定的系统提供给用户。**ZooKeeper也可以实现注册中心的功能**

- 生产者注册到ZK：新增生产者模块`cloud-provider-payment8004`：

    - Docker部署ZooKeeper`docker run --name zookeeper -p 2181:2181 -d zookeeper`

        - [使用 Docker 一步搞定 ZooKeeper 集群的搭建 - 后台开发 - SegmentFault 思否](https://segmentfault.com/a/1190000006907443)
        - [Docker安装Zookeeper并进行操作_大数据_Radom&7-CSDN博客](https://blog.csdn.net/qq_26641781/article/details/80886831)

    - idea安装ZK客户端插件：

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

    - 3、编写简单的Controller类(不再演示和数据库的交互)

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

    - 4、启动主启动类，看服务是否已经注册到ZK：

        ![b2aDx7](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/b2aDx7.png)

        ![XxVmMo](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/XxVmMo.png)

        ![km2CHq](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/km2CHq.png)

    - 解决jar包与ZK版本不一致的问题：先排除，再引入特定版本

        ```xml
        <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
                    <exclusions>
                        <!--先排除自带的zookeeper3.5.3-->
                        <exclusion>
                            <groupId>org.apache.zookeeper</groupId>
                            <artifactId>zookeeper</artifactId>
                        </exclusion>
                    </exclusions>
                </dependency>
                <!--再引入zookeeper3.4.9版本-->
                <dependency>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                    <version>3.4.14</version>
                </dependency>
        ```

- 临时节点还是持久节点？

    - [ZooKeeper 节点类型_大数据_和大黄的博客-CSDN博客](https://blog.csdn.net/heyutao007/article/details/38741207)
    - 停止8004微服务后，等待一段时间后ZK客户端中就会注销该服务
    - ZK管理微服务使用的是临时性节点，心跳断连后直接注销服务

- 消费者注册到ZK, 增加模块`cloud-consumer-zkorder801`

    - POM、主配置文件与8004几乎一致

    - 编写配置类注入restTemplate，用于远程调用8004端口提供的微服务

        ```java
        @Configuration
        @LoadBalanced
        public class ApplicationContextConfig {
            @Bean
            public RestTemplate getRestTemplate(){
                return new RestTemplate();
            }
        }
        ```

    - Controller如下：

        ```java
        @RestController
        @Slf4j
        public class OrderZkController {
            // 调用地址
            public static final String INVOKE_URL = "http://cloud-provider-payment";
        
            @Resource
            private RestTemplate restTemplate;
        
            @GetMapping(value = "/consumer/payment/zk")
            public String paymentInfo(){
                String result = restTemplate.getForObject(INVOKE_URL + "/payment/zk", String.class );
                return result;
            }
        }
        ```

- 效果：

    ![an2z10](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/an2z10.png)

    ![YtncbF](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/YtncbF.png)

- 拓展：
    - [zookeeper集群搭建 - 掘金](https://juejin.im/post/5ba879ce6fb9a05d16588802)
    - [构建高可用ZooKeeper集群 - cyfonly - 博客园](https://www.cnblogs.com/cyfonly/p/5626532.html)

> 实际开发环境中用ZK完全替代Eureka的地方不多



### 8 Consul实现注册中心

[Spring Cloud Consul 中文文档 参考手册 中文版](https://www.springcloud.cc/spring-cloud-consul.html)

- **Consul** 是一个支持多数据中心分布式高可用的服务发现和配置共享的服务软件,由 HashiCorp 公司用 Go 语言开发, 基于 Mozilla Public License 2.0 的协议进行开源. 
- Consul具有以下特性：
    - **服务发现** Consul的客户端可用提供一个服务,比如 api 或者mysql ,另外一些客户端可用使用Consul去发现一个指定服务的提供者.通过DNS或者HTTP应用程序可用很容易的找到他所依赖的服务.
    - **健康检查** Consul客户端可用提供任意数量的健康检查,指定一个服务(比如:webserver是否返回了200 OK 状态码)或者使用本地节点(比如:内存使用是否大于90%). 这个信息可由operator用来监视集群的健康.被服务发现组件用来避免将流量发送到不健康的主机.
    - **Key/Value存储** 应用程序可用根据自己的需要使用Consul的层级的Key/Value存储.比如动态配置,功能标记,协调,领袖选举等等,简单的HTTP API让他更易于使用.
    - **多数据中心**: Consul支持开箱即用的多数据中心.这意味着用户不需要担心需要建立额外的抽象层让业务扩展到多个区域.
- 安装`brew install consul`

### 8.1 服务提供者注册进consul

- 新建新模块`cloud-provider-consul-payment-8006`

    - POM:

        ```xml
        <!--SpringCloud consul-server-->
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
                </dependency>
        ```

    - 主配置文件

        ```yaml
        server:
          port: 8006
        spring:
          application:
            name: consul-provider-payment
          cloud:
            consul:
              host: 127.0.0.1
              port: 8500
              discovery:
                service-name: ${spring.application.name}
        
        ```

    - 编写主启动类

    - 编写Controller，同ZK：

        ```java
        @RestController
        @Slf4j
        public class ConsulPaymentController {
            @Value("${server.port}") // 可以读取配置文件中${server.port}的值， 目的是在结果页面显示本次查询由哪一台服务器提供
            private String serverPort;
        
            @RequestMapping(value = "/payment/consul")
            public String paymentConsul(){
                return "Spring Cloud with Consul:" + serverPort + "\t" + UUID.randomUUID().toString();
            }
        }
        ```

    - 启动8006服务

        ![j4QBej](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/j4QBej.png)

        ![HczrLI](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/HczrLI.png)

### 8.2 服务消费者注册进consul

- 新建`cloud-consumer-consul-order801`模块
    - 修改POM、编写配置文件、主启动类，ApplicationContextConfig声明RestTemplate为@Bean
    - 编写Controller
- ![46z0LU](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/46z0LU.png)
- ![fwycEk](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/fwycEk.png)

### 9 三种注册中心的异同点

| 组件名    | 语言 | CAP    | 服务健康检查 | 对外暴露接口 | 集成SpringCloud |
| --------- | ---- | ------ | ------------ | ------------ | --------------- |
| Eureka    | Java | **AP** | 可配支持     | HTTP         | Yes             |
| Consul    | Go   | **CP** | 支持         | HTTP/DNS     | Yes             |
| Zookeeper | Java | **CP** | 支持         | ZK客户端     | Yes             |

- 比较：
    - Eureka需要新建一个单独的项目导入`eureka-server`jar包并运行启动，成为一个运行的注册中心。例如我们之前编写的模块`cloud-eureka-server-7001`, `cloud-eureka-server-7002`。我们可以在该项目的主配置文件中对Eureka服务器进行配置
    - Zookeeper和Consul都只需要运行在特定主机或者Docker容器中即可，我们只需再编写生产者和消费者模块直接调用它们提供的注册中心服务即可
    - Eureka和Consul具有后台管理页面，ZK需要连接ZK客户端
- **AP（Eureka）**
    - ![dZbTip](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/dZbTip.png)
    - Eureka具有自我保护机制，偶尔出现服务失效（数据同步失败），Eureka也不会立即注销服务
- **CP(Zookeeper / Consul)**
    - ![XB6yxF](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/XB6yxF.png)
    - ZK/Consul在心跳失效后会立即注销服务

### 10  分布式与CAP理论

- 在[理论计算机科学](https://zh.wikipedia.org/wiki/%E7%90%86%E8%AB%96%E8%A8%88%E7%AE%97%E6%A9%9F%E7%A7%91%E5%AD%B8)中，**CAP定理**（CAP theorem），又被称作**布鲁尔定理**（Brewer's theorem），它指出对于一个[分布式计算系统](https://zh.wikipedia.org/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E8%AE%A1%E7%AE%97)来说，不可能同时满足以下三点：
    - <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/Al5YkZ.jpg" alt="Al5YkZ" style="zoom:50%;" />
      - 一致性（**C**onsistency） （等同于所有节点访问同一份最新的数据副本）
      - 可用性（**A**vailability）（每次请求都能获取到非错的响应——但是不保证获取的数据为最新数据）
      - 分区容错性（**P**artition tolerance）（以实际效果而言，分区相当于对通信的时限要求。系统如果不能在时限内达成数据一致性，就意味着发生了分区的情况，必须就当前操作在C和A之间做出选择。）
    - 这个定理起源于[加州大学柏克莱分校](https://zh.wikipedia.org/wiki/加州大學柏克萊分校)（University of California, Berkeley）的计算机科学家[埃里克·布鲁尔](https://zh.wikipedia.org/wiki/埃里克·布鲁尔)在2000年的[分布式计算原理研讨会](https://zh.wikipedia.org/w/index.php?title=分佈式計算原理研討會&action=edit&redlink=1)（PODC）上提出的一个[猜想](https://zh.wikipedia.org/wiki/猜想)。在2002年，[麻省理工学院](https://zh.wikipedia.org/wiki/麻省理工学院)（[MIT](https://zh.wikipedia.org/wiki/麻省理工学院)）的[赛斯·吉尔伯特](https://zh.wikipedia.org/w/index.php?title=赛斯·吉尔伯特&action=edit&redlink=1)和[南希·林奇](https://zh.wikipedia.org/w/index.php?title=南希·林奇&action=edit&redlink=1)发表了布鲁尔猜想的证明，使之成为一个[定理](https://zh.wikipedia.org/wiki/定理)。
- CAP定理的三种组合方式：
    - [分布式系统之CAP原理 - heapStark - 博客园](https://www.cnblogs.com/heapStark/p/8351852.html)
    - **CA without P**：如果不要求P（不允许分区），则C（强一致性）和A（可用性）是可以保证的。但其实分区不是你想不想的问题，而是始终会存在，**CA系统基本上是单机系统，比如单机数据库**。2PC是实现强一致性的具体手段。
        - <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/nzeAMT.jpg" alt="nzeAMT" style="zoom:67%;" />
    - **CP without A**：如果不要求A（可用），相当于每个请求都需要在Server之间强一致，而P（分区）会导致同步时间无限延长，如此CP也是可以保证的。很多传统的数据库分布式事务都属于这种模式。
        - <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/l6nBxO.jpg" alt="l6nBxO" style="zoom:67%;" />
    - **AP wihtout C**：要高可用并允许分区，则需放弃一致性。一旦分区发生，节点之间可能会失去联系，为了高可用，每个节点只能用本地数据提供服务，而这样会导致全局数据的不一致性。现在众多的NoSQL都属于此类。
        - <img src="https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/6on7li.jpg" alt="6on7li" style="zoom:67%;" />

### 11  使用Ribbon实现负载均衡

#### 11.1 负载均衡介绍

- **负载均衡是什么？**——简单的说负载均衡就是将用户请求平摊分配到多个服务上，从而达到系统的高可用。常见的负载均衡软件有Nginx, LVS等
- **Ribbon本地负载均衡客户端 VS Nginx服务端负载均衡**
    - **Nginx是服务器负载均衡**,客户端所有请求都会交给 Nginx,然后由 Nginx实现转发请求。即负载均衡是由服务端实现的。
    - **Ribbon本地负载均衡**,在调用微服务接口时候,会在注册中心上获取注册信息服务列表之后缓存到JVM本地,从而在本地实现RPC远程服务调用技术
    - **进程内LB**: 将LB逻辑集成到消费方,消费方从服务注册中心获知有哪些地址可用,然后**自己**再从这些地址中选择出个合适的服务器。Ribbon就属于进程內LB,它只是一个类库,集成于消费方进程,消费方通过它来获取到服务提供方的地址。
    - **集中式LB**: 即在服务的消费方和提供方之间使用独立的LB设施(可以是硬件,如F5,也可以是软件,如 ngInx)由该设施负责把访问请求通过某种策略转发至服务的提供方;

- Spring Cloud **Ribbon是一个基于HTTP和TCP的客户端（消费者）负载均衡工具**，它基于Netflix Ribbon实现。通过Spring Cloud的封装，可以让我们轻松地将面向服务的REST模版请求自动转换成客户端负载均衡的服务调用。Ribbon也已经进入维护模式。

    - **简单的说，Ribbon就是负载均衡+RestTemplate调用**
    - 总结: Ribbon其实就是一个软负载均衡的客户端组件,他可以和其他所需请求的客户端结合使用,和eureka结合只是其中的一个实例。
    - ![o2bCCU](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/o2bCCU.png)

    > 注意：Ribbon是在客户端实现负载均衡的，因此只需要在客户端（消费者）的微服务模块中引入Ribbon相关依赖，服务端与Ribbon无关（Ribbon对服务端是透明的）。

- Ribbon在工作时分成两步：

    - 第一步先选择 EurekaServer,它优先选择在同一个区域内负载较少的 server.
    - 第二步再根据用户指定的策略,再从serer到的服务注册列表中选择个地址。其中 **Ribbon提供了多种策略:比如轮询、随机和根据响应时间加权**。

- **引入`spring-cloud-starter-netflix-eureka-client`会自动引入Ribbon**![bBFENX](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/bBFENX.png)

#### 11.2 使用其他Ribbon自带的负载均衡算法

- **IRule接口**,Riboon使用该接口,根据特定算法从所有服务中,选择一个服务。Rule接口有7个实现类,每个实现类代表一个负载均衡算法
- Ribbon默认的负载均衡算法是轮询`RoundRobinRule`。轮询算法RoundRobinRule：`rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标`。每次服务重启动后rest接口计数从1开始

- ![hQWXCp](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/hQWXCp.png)

- ![NOHW0n](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/NOHW0n.png)

- 如何修改Ribbon使用其他的自带的负载均衡算法？

    - 1、编写配置类`com.example.myrule.MySelfRule`

        - 注意：这个自定义配置类不能放在`@ ComponentScan`所扫描的当前包下以及子包下，否则我们自定义的这个配置类就会被所有的 Ribbon客户端所共享,达不到特殊化定制的目的了。
        - 不能放在`com.example.springcloud.OrderMain801`所属的`com.example.springcloud`包下，可以创建一个`com.example.myrule`包

        ```java
        @Configuration
        public class MySelfRule {
        
            @Bean
            public IRule myRule(){
                return new RandomRule(); //更换Ribbon的负载均衡算法为随机
            }
        }
        
        ```

    - 2、在客户端（消费者）的主启动类上添加**@RibbonClient**注解

        ```java
        // 客户端
        @SpringBootApplication
        @EnableEurekaClient
        @RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)
        public class OrderMain801 {
            public static void main(String[] args) {
                SpringApplication.run(OrderMain801.class, args);
            }
        }
        ```

    - 3、启动后访问`http://127.0.0.1:801/consumer/payment/getForEntity/1`测试，负载均衡算法已经变为随机。

#### 11.3自定义Ribbon的负载均衡算法

- [手写轮询算法](https://www.bilibili.com/video/BV18E411x7eT?p=42)

### 12 OpenFeign服务调用

#### 12.1  Feign介绍

[Ribbon、Feign和OpenFeign的区别_Java_紫眸的博客-CSDN博客](https://blog.csdn.net/zimou5581/article/details/89949852)

- **Feign** 是一个声明web服务客户端，这便得编写web服务客户端更容易，使用Feign 创建一个接口并对它进行注解，它具有可插拔的注解支持包括Feign注解与JAX-RS注解，Feign还支持可插拔的编码器与解码器，Spring Cloud 增加了对 Spring MVC的注解，Spring Web 默认使用了HttpMessageConverters, Spring Cloud 集成 Ribbon 和 Eureka 提供的负载均衡的HTTP客户端 Feign.
- **OpenFeign**是Spring Cloud 在Feign的基础上支持了Spring MVC的注解，如@RequesMapping等等。OpenFeign的@FeignClient可以解析SpringMVC的@RequestMapping注解下的接口，并通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务。

- 前面在使用 Ribbon+ RestTemplatel时,利用 RestTemplate对http请求的封装处理,形成了一套模版化的调用方法。但是在实际开发中,由于对服务依赖的调用可能不止一处,往往一个接口会被多处调用,所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用。**所以, Feign在此基础上做了进一步封装,由他来帮助我们定义和实现依赖服务接口的定义。**
- 在 Feign的实现下,我们只需创建一个接口并使用注解的方式来配置它（以前是Dao接口上面标注 Mapper注解， 现在是一个微服务接口上面标注一个Feign注解即可) ,即可完成对服务提供方的接口绑定,简化了操作
- 总结：**在客户端（消费者）中定义一个和服务器端（生产者）一样的服务接口，并使用Feign的注解标注。即可远程调用生产者的提供的服务**。**不在使用RestTemplate**

#### 12.2 使用OpenFeign改造消费者模块

- 1、新建一个消费者`cldou-consumer-feign-order801`模块

    - 引入OpenFeign的POM依赖，OpenFeign本身会引入Ribbon

    - 编写主配置文件

        ```yaml
        server:
          port: 801
        eureka:
          client:
            register-with-eureka: false
            fetch-registry: true
            service-url:
              defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
        ```

    - 编写主启动类，**并标注`@EnableFeignClients`开启使用Feign的注解**

    - 编写与生产者对应的服务调用接口，并标注**@FeignClient(value = "SERVICE-NAME")**, SERVICE-NAME与Eureka中注册的生产者服务名一致

        ```java
        @FeignClient(value = "CLOUD-PAYMENT-SERVICE")
        public interface PaymentFeignService {
        
            @GetMapping(value = "/payment/get/{id}") // 指定要调用的生产者微服务的url
            public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);
        }
        ```

    - 编写Controller

        ```java
        @RestController
        @Slf4j
        public class OrderFeignController {
        
            @Resource
            private PaymentFeignService paymentFeignService;
        
            @GetMapping(value = "/consumer/payment/get/{id}")
            public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
                return paymentFeignService.getPaymentById(id);
            }
        }
        ```

    - 启动测试

#### 12.3 OpenFeign超时机制

- OpenFeign底层使用了Ribbon做服务调用（负载均衡），客户端默认只会等待生产者服务1秒钟，如果超时则返回报错
- 可以在配置文件中修改，对Ribbon的超时时间作出更改

- （一）模拟生产者超时

    - 1、在生产者Controller中加入下面的方法：

        ```java
            @GetMapping(value = "/payment/feign/timeout")
            public String paymentFeignTimeout(){
                try {
                    TimeUnit.SECONDS.sleep(3);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                return serverPort;
            }
        ```

    - 2、在feign消费者上增加对应的接口:

        ```java
        @FeignClient(value = "CLOUD-PAYMENT-SERVICE")
        public interface PaymentFeignService {
            @GetMapping(value = "/payment/feign/timeout")
            public String paymentFeignTimeout();
        }
        
        ```

    - 3、在feign消费者修改Controller:

        ```java
        @RestController
        @Slf4j
        public class OrderFeignController {
        
            @Resource
            private PaymentFeignService paymentFeignService;
        
            @GetMapping(value = "/consumer/payment/feign/timeout")
            public String paymentFeignTimeout(){
                // OpenFeign底层使用了Ribbon做服务调用（负载均衡），客户端默认只会等待1秒钟
                return paymentFeignService.paymentFeignTimeout();
            }
        
        }
        ```

    - 效果：
        - 访问http://127.0.0.1:8001/payment/feign/timeout， 页面3s后显示端口
        - 访问http://127.0.0.1:801/consumer/payment/feign/timeout， 页面1s之后报错：![image-20200426215907389](../../Library/Application Support/typora-user-images/image-20200426215907389.png)

- （二）在配置文件中设置Ribbon超时时间：

    > 因为Ribbon是客户端（消费者）上的负载均衡工具，因此此时要修改的是客户端`cldou-consumer-feign-order801`中的主配置文件

    ```yaml
    server:
      port: 801
    eureka:
      client:
        register-with-eureka: false
        fetch-registry: true
        service-url:
          defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
    
    ## 设置feign客户端超时时间(OpenFeign默认支持ribbon)
    ribbon:
      # 指的是建立连接后从服务器读取到可用资源所用的时间(单位ms)
      ReadTimeout: 5000
      # 指的是建立连接所用的时间,适用于网络状态正常的情况下,两端连接所用的时间(单位ms)
      ConnectTimeout: 5000
    ```

    再次测试即正常等待3s后显示端口：![foC8eV](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2020/04/foC8eV.png)

#### 12.4 OpenFeign的日志记录功能

[OpenFeign 日志打印功能_cloud_TheNoOne--的博客-CSDN博客](https://blog.csdn.net/qq_41211642/article/details/104851537)

- OpenFeign 提供了日志打印功能，可以通过配置来调整日志级别，从而了解 Feign 中 Http 请求的细节。说白了就是对接口的调用情况进行监控和输出

- **OpenFeign日志级别：**

    - NONE：默认的，不显示任何日志
    - BASIC：仅记录请求方法、URL、响应状态码及执行时间
    - HEADERS：除了 BASIC 中定义的信息之外，还有请求和响应的头信息
    - FULL：除了 HEADERS 中定义的信息之外，还有请求和响应的正文及元数据

- 配置日志的方法：

    - 1、在Feign消费者服务模块配置日志Bean: 新建Config类，加入日志Bean

        ```java
        @Configuration
        public class FeignConfig {
            @Bean
            Logger.Level feignLoggerLevel(){
                return Logger.Level.FULL;
            }
        }
        ```

    - 2、修改Feign消费者模块的配置文件：

        ```yaml
        logging:
          level:
            # feign日志以什么级别监控哪个接口（全路径类名: 日志级别）
            com.example.springcloud.service.PaymentFeignService: debug
        ```

    - 3、启动。访问 http://127.0.0.1:801/consumer/payment/get/2 , 控制台输出以下日志：

        ```scheme
        2020-04-26 22:28:57.488 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] <--- HTTP/1.1 200 (235ms)
        2020-04-26 22:28:57.489 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] connection: keep-alive
        2020-04-26 22:28:57.489 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] content-type: application/json
        2020-04-26 22:28:57.489 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] date: Sun, 26 Apr 2020 14:28:57 GMT
        2020-04-26 22:28:57.489 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] keep-alive: timeout=4
        2020-04-26 22:28:57.489 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] proxy-connection: keep-alive
        2020-04-26 22:28:57.489 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] transfer-encoding: chunked
        2020-04-26 22:28:57.489 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] 
        2020-04-26 22:28:57.491 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] {"code":200,"message":"serverPort:8001查询数据成功","data":{"id":2,"serial":"测试2"}}
        2020-04-26 22:28:57.491 DEBUG 51420 --- [-nio-801-exec-1] c.e.s.service.PaymentFeignService        : [PaymentFeignService#getPaymentById] <--- END HTTP (93-byte body)
        ```

        

    







## 参考资料

- [1.5W 字搞懂 Spring Cloud，太牛了！](https://mp.weixin.qq.com/s/EHPKm50KmHq_KZIHyVef3A)

- [SpringCloud(H版)学习---服务注册中心 - coder、 - 博客园](https://www.cnblogs.com/rmxd/p/12547231.html)
- [espmihacker/cloud2020: 尚硅谷2020最新版SpringCloud(H版&alibaba)框架开发教程全套完整版从入门到精通(大牛讲授spring cloud)](https://github.com/espmihacker/cloud2020)
- [SpringMVC @ResponseBody和@RequestBody使用 - 简书](https://www.jianshu.com/p/7097fea8ce3f)
- [springcloud(二)：注册中心Eureka - 纯洁的微笑 - 博客园](https://www.cnblogs.com/ityouknow/p/6854805.html)
- [微服务注册中心Eureka架构深入解读 - InfoQ](https://www.infoq.cn/article/jlDJQ*3wtN2PcqTDyokh)
- [IDEA Services 工具窗口: 一个管理所有服务的地方【译】 - 简书](https://www.jianshu.com/p/2e2332f247fe)
- [zookeeper集群搭建 - 掘金](https://juejin.im/post/5ba879ce6fb9a05d16588802)
- [Consul是什么 · Consul入门指南](https://book-consul-guide.vnzmi.com/01_what_is_consul.html)
- [Spring Cloud Consul 中文文档 参考手册 中文版](https://www.springcloud.cc/spring-cloud-consul.html)
- [CAP理论中的P到底是个什么意思？ - 知乎](https://www.zhihu.com/question/54105974)
- [分布式系统之CAP原理 - heapStark - 博客园](https://www.cnblogs.com/heapStark/p/8351852.html)
- [分布式CAP理论 - 简书](https://www.jianshu.com/p/ecc14fc291a8)
- [Ribbon详解 - 简书](https://www.jianshu.com/p/1bd66db5dc46)
- [Ribbon、Feign和OpenFeign的区别_Java_紫眸的博客-CSDN博客](https://blog.csdn.net/zimou5581/article/details/89949852)