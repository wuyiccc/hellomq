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


    // nameserver属性
    private String nameserverIp;

    private Integer nameserverPort;

    private String nameserverUser;

    private String nameserverPassword;

    private Integer brokerPort;

    public String getHelloMqHome() {
        return helloMqHome;
    }

    public void setHelloMqHome(String helloMqHome) {
        this.helloMqHome = helloMqHome;
    }

    public String getNameserverIp() {
        return nameserverIp;
    }

    public void setNameserverIp(String nameserverIp) {
        this.nameserverIp = nameserverIp;
    }

    public Integer getNameserverPort() {
        return nameserverPort;
    }

    public void setNameserverPort(Integer nameserverPort) {
        this.nameserverPort = nameserverPort;
    }

    public String getNameserverUser() {
        return nameserverUser;
    }

    public void setNameserverUser(String nameserverUser) {
        this.nameserverUser = nameserverUser;
    }

    public String getNameserverPassword() {
        return nameserverPassword;
    }

    public void setNameserverPassword(String nameserverPassword) {
        this.nameserverPassword = nameserverPassword;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }
}
