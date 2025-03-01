package com.example.alibaba.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.springcloud.entities.CommonResult;
import com.example.springcloud.entities.Payment;

public class MyHandler {
    // 注意一定要static
    public static CommonResult<Payment> handlerException1(BlockException e) {
        return new CommonResult<>(444, "按照用户自定义1，全局的", new Payment(2020L, "serial004----1"));
    }

    public static CommonResult<Payment> handlerException2(BlockException e) {
        return new CommonResult<>(444, "按照用户自定义2，全局的", new Payment(2020L, "serial004----2"));
    }
}