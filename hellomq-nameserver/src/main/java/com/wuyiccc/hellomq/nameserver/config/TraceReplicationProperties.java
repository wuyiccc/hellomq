package com.wuyiccc.hellomq.nameserver.config;

/**
 * @author wuyiccc
 * @date 2024/10/14 22:21
 */
public class TraceReplicationProperties {

    private String nextNode;

    private Integer port;

    public String getNextNode() {
        return nextNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
