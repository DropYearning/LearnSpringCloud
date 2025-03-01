package com.example.springcloud.alibaba.service;

import com.example.springcloud.alibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-storage-service")
public interface StorageService {
    /**
     * 减库存
     * @param productId
     * @param count
     * @return
     */
    @PostMapping(value = "storage/decrease") //发的是POST请求
    CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}