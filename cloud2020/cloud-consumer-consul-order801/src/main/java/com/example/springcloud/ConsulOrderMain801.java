package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsulOrderMain801 {
    public static void main(String[] args) {
        SpringApplication.run(ConsulOrderMain801.class, args);
    }
}
