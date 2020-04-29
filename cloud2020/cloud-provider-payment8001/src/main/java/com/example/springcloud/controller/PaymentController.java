package com.example.springcloud.controller;

import com.example.springcloud.entities.CommonResult;
import com.example.springcloud.entities.Payment;
import com.example.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController // Spring4之后新加入的注解，原来返回json.相当于@ResponseBody和@Controller配合。
@Slf4j // 如果不想每次都写private final Logger logger = LoggerFactory.getLogger(当前类名.class); 可以用注解@Slf4j;

public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}") // 可以读取配置文件中${server.port}的值， 目的是在结果页面显示本次查询由哪一台服务器提供
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create") // 写操作Post
    //@RequestBody 注解用于将Controller的方法参数，根据HTTP Request Header的content-Type的内容,通过适当的HttpMessageConverter转换为JAVA类
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*****插入结果:" + result);
        if (result > 0) {
            return new CommonResult(200, "serverPort:" + serverPort + "插入数据成功", result);
        }else {
            return new CommonResult(444, "serverPort:" + serverPort + "插入数据失败", null);
        }
    }

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

    @GetMapping(value = "/payment/discovery")
    public Object discovery(){

        // 获得Eureka中注册所有服务名
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("*****com.example.springcloud.service:" + service);
        }

        // 获得Eureka中注册的CLOUD-PAYMENT-SERVICE服务的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }

        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }


    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return serverPort;
    }


    @GetMapping(value = "/payment/zipkin")
    public String paymentZipkin() {
        return "Zipkin~~~~ O(∩_∩)O哈哈~";
    }
}
