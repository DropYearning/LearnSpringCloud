package com.example.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {


    // 正常访问方法
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "  paymentInfo_OK, id: " + id + "\t" + " O(∩_∩)O哈哈~";
    }

    // ===============================================服务降级===========================================================
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") // 约定响应时间超过5s异常，5s以内正常
    })
    public String paymentInfo_Timeout(Integer id) {
        int timeNumber = 3;
        // 线程睡timeNumber秒
        //int timeNumber = 10/0;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + " paymentInfo_Timeout, id: " + id + "\t" + " 耗时"+ timeNumber +"秒";
    }

    // paymentInfo_Timeout的兜底方法
    public String paymentInfo_TimeoutHandler(Integer id){
        return "线程池：" + Thread.currentThread().getName() + " 8001自检系统繁忙，请稍后再试, id: " + id + "\t" + "o(╥﹏╥)o";
    }

    // ===============================================服务熔断===========================================================

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),               //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),    //时间窗口
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")        //失败率多少次跳闸
            // 如果10次请求内有60%失败，则触发熔断。时间窗口期一过，断路器会变为半开状态，尝试让一些请求通过，如果都成功则会恢复至正常服务的状态
    })
    public String paymentCircuitBreaker(Integer id) {
        if(id < 0) {
            throw new RuntimeException("id不能是负数！");
        }
        String serial = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serial;
    }

    public String paymentCircuitBreaker_fallback(Integer id) {
        return "CircuitBreaker，请稍后再试~~~" + id;
    }

}
