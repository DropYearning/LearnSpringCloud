package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class HystrixPaymentMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixPaymentMain8001.class, args);
    }

    //@Bean
    //public ServletRegistrationBean getServlet() {
    //    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
    //    ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
    //    registrationBean.setLoadOnStartup(1);
    //    registrationBean.addUrlMappings("/hystrix.stream");
    //    registrationBean.setName("HystrixMetricsStreamServlet");
    //    return registrationBean;
    //}
}
