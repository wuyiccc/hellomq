package com.wuyiccc.hellomq.nameserver.event.model;

/**
 * @author wuyiccc
 * @date 2024/10/5 22:45
 *
 * 注册事件
 */
public class RegistryEvent extends Event {


    private String user;

    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
