package com.example.springcloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

// Feign + 接口 + 注解 实现远程调用
// fallback中指定的PaymentHystrixService接口实现类为接口方法的调用做fallback
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", fallback = PaymentFallbackService.class)
//@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT")
public interface PaymentHystrixService {

    @GetMapping(value = "/payment/hystrix/ok/{id}")
    String paymentInfo_OK(@PathVariable(value = "id") Integer id);

    @GetMapping(value = "/payment/hystrix/timeout/{id}")
    String paymentInfo_Timeout(@PathVariable(value = "id") Integer id);
}
