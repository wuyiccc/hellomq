package com.wuyiccc.hellomq.nameserver.store;

import com.wuyiccc.hellomq.common.constants.StrConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuyiccc
 * @date 2024/10/6 10:57
 */
public class ServiceInstanceManager {

    private Map<String, ServiceInstance> serviceInstanceMap = new ConcurrentHashMap<>();


    // 如果存在则更新
    public void putIfExist(ServiceInstance serviceInstance) {

        ServiceInstance currentInstance = this.get(serviceInstance.getBrokerIp(), serviceInstance.getBrokerPort());
        if (currentInstance != null && currentInstance.getFirstRegistryTime() != null) {
            // 保留注册时间不更新
            serviceInstance.setFirstRegistryTime(currentInstance.getFirstRegistryTime());
        }
        serviceInstanceMap.put(serviceInstance.getBrokerIp() + StrConstants.COLON + serviceInstance.getBrokerPort(), serviceInstance);
    }


    public void put(ServiceInstance serviceInstance) {

        serviceInstanceMap.put(serviceInstance.getBrokerIp() + StrConstants.COLON + serviceInstance.getBrokerPort(), serviceInstance);
    }

    public ServiceInstance get(String brokerIp, Integer brokerPort) {

        return serviceInstanceMap.get(brokerIp + StrConstants.COLON + brokerPort);
    }

    public ServiceInstance remove(String key) {

        return serviceInstanceMap.remove(key);
    }

    public Map<String, ServiceInstance> getServiceInstanceMap() {
        return serviceInstanceMap;
    }
}
