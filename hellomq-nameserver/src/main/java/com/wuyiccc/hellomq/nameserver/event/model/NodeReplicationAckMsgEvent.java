package com.wuyiccc.hellomq.nameserver.event.model;

/**
 * @author wuyiccc
 * @date 2024/10/20 22:35
 */
public class NodeReplicationAckMsgEvent extends Event {

    private String nodeIp;

    private Integer nodePort;

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }


    public Integer getNodePort() {
        return nodePort;
    }

    public void setNodePort(Integer nodePort) {
        this.nodePort = nodePort;
    }
}
