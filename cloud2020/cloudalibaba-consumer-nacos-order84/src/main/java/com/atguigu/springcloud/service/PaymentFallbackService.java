package com.atguigu.springcloud.service;


import com.example.springcloud.entities.CommonResult;
import com.example.springcloud.entities.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentService {
    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return new CommonResult<>(444, "服务降级 ---- fallback");
    }
}
