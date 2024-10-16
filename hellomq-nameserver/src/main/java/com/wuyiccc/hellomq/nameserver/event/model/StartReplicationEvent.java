package com.wuyiccc.hellomq.nameserver.event.model;

/**
 * @author wuyiccc
 * @date 2024/10/16 21:07
 */
public class StartReplicationEvent extends Event {


    private String user;

    private String password;

    private String slaveIp;

    private String slavePort;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSlaveIp() {
        return slaveIp;
    }

    public String getSlavePort() {
        return slavePort;
    }
}
