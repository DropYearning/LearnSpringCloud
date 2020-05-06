package com.example.springcloud.alibaba.dao;

import com.example.springcloud.alibaba.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {

    int create(Order order); // 下订单方法

    /**
     * 修改订单状态，0——>1
     * @param userId
     * @param status
     * @return
     */
    int update(@Param("userId") Long userId, @Param("status") Integer status); // 更新订单状态

}
