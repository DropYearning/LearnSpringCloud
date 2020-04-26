package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableEurekaClient // 没有将本服务注册到Eureka，不用加这个注解
public class OrderFeignMain801 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain801.class, args);
    }
}
