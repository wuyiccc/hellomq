package com.wuyiccc.hellomq.nameserver.cache;

import com.wuyiccc.hellomq.nameserver.core.PropertiesLoader;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstanceManager;

/**
 * @author wuyiccc
 * @date 2024/10/6 10:38
 */
public class CommonCache {

    private static PropertiesLoader propertiesLoader = new PropertiesLoader();


    private static ServiceInstanceManager serviceInstanceManager = new ServiceInstanceManager();



    public static PropertiesLoader getPropertiesLoader() {
        return propertiesLoader;
    }

    public static void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        CommonCache.propertiesLoader = propertiesLoader;
    }

    public static ServiceInstanceManager getServiceInstanceManager() {
        return serviceInstanceManager;
    }

    public static void setServiceInstanceManager(ServiceInstanceManager serviceInstanceManager) {
        CommonCache.serviceInstanceManager = serviceInstanceManager;
    }
}
