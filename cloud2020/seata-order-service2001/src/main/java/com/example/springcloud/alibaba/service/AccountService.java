package com.example.springcloud.alibaba.service;

import com.example.springcloud.alibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "seata-account-service")
public interface AccountService {
    /**
     * 减余额
     * @param userId
     * @param money
     * @return
     */
    @PostMapping(value = "account/decrease") //发的是POST请求
    CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}