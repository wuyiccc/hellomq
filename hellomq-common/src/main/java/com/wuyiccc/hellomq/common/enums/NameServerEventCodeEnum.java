package com.wuyiccc.hellomq.common.enums;

/**
 * @author wuyiccc
 * @date 2024/10/5 22:43
 */
public enum NameServerEventCodeEnum {

    REGISTRY(1, "注册事件"),
    UN_REGISTRY(2, "下线事件"),
    HEART_BEAT(3, "心跳事件"),

    // 从节点发送
    START_REPLICATION(4, "开启复制"),


    // 主节点方式
    MASTER_START_REPLICATION_ACK(5, "master回应slave节点开启同步"),

    // 主节点发送
    MASTER_REPLICATION_MSG(6, "主从同步数据"),

    // 从节点发送
    SLAVE_HEART_BEAT(7,"从节点心跳数据"),

    SLAVE_REPLICATION_ACK_MSG(8, "从节点接收同步数据成功"),

    NODE_REPLICATION_MSG(9, "节点复制数据"),

    NODE_REPLICATION_ACK_MSG(10, "链式复制中数据同步完成信号"),
    ;

    private int code;

    private String desc;

    NameServerEventCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
