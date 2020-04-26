package com.example.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MyLoadBalancer implements LoadBalancer {
    @Override
    public ServiceInstance instance(List<ServiceInstance> serviceInstances) {
        return null;
    }
}
