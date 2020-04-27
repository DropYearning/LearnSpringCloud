package com.example.springcloud.controller;

import com.example.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "orderGlobalFallbackMethod")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    @HystrixCommand
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        //int a = 10/0;
        String result = paymentHystrixService.paymentInfo_OK(id);
        return  result;
    }


    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500") // 客户端只愿意等待1.5s
    })
    String paymentInfo_Timeout(@PathVariable("id") Integer id){
        //int a = 10/0;
        String result = paymentHystrixService.paymentInfo_Timeout(id);
        return  result;
    }

    public String paymentInfo_TimeoutHandler(@PathVariable(value = "id") Integer id) {
        System.out.println("paymentInfo_TimeoutHandler执行了" + ":消费者801: 对方支付系统繁忙，请稍后再试");
        return "消费者801: 对方支付系统繁忙，请稍后再试";
    }

     // 全局fallback方法
    public String orderGlobalFallbackMethod() {
        return "消费者801:通用fallback方法被执行了！";
    }


}