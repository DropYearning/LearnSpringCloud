package com.example.springcloud.alibaba.service;

import com.example.springcloud.alibaba.domain.Order;

public interface OrderService {
    /**
     * 创建订单
     * @param order
     */
    void create(Order order);
}