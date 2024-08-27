package com.wuyiccc.hellomq.broker.config;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:23
 */
public class GlobalProperties {


    /**
     * 读取环境变量中配置的mq地址
     */
    private String helloMqHome;


    public String getHelloMqHome() {
        return helloMqHome;
    }

    public void setHelloMqHome(String helloMqHome) {
        this.helloMqHome = helloMqHome;
    }
}
