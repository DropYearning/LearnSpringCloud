package com.example.springcloud.alibaba.service.impl;

import com.example.springcloud.alibaba.dao.OrderDao;
import com.example.springcloud.alibaba.domain.Order;
import com.example.springcloud.alibaba.service.AccountService;
import com.example.springcloud.alibaba.service.OrderService;
import com.example.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private AccountService accountService;
    @Resource
    private StorageService storageService;

    /**
     * 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态
     * GlobalTransactional seata开启分布式事务,异常时回滚,name保证唯一即可
     * @param order
     */
    @Override
    @GlobalTransactional(name = "cloud-create-order", rollbackFor = Exception.class) // 发生任何异常都回滚
    public void create(Order order) {
        // 1 新建订单
        log.info("----->开始新建订单");
        orderDao.create(order);

        // 2 扣减库存
        log.info("----->订单微服务开始调用库存,做扣减Count");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("----->订单微服务开始调用库存,做扣减End");

        // 3 扣减账户
        log.info("----->订单微服务开始调用账户,做扣减Money");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("----->订单微服务开始调用账户,做扣减End");

        // 4 修改订单状态,从0到1,1代表已完成
        log.info("----->修改订单状态开始");
        orderDao.update(order.getUserId(), 0); //刚创建订单，因此状态为0
        log.info("----->下订单结束了,O(∩_∩)O哈哈~");
    }
}
