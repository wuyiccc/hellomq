package com.wuyiccc.hellomq.nameserver.enums;

/**
 * @author wuyiccc
 * @date 2024/10/19 21:21
 *
 * 主从同步的复制模式
 */
public enum MasterSlaveReplicationTypeEnum {

    SYNC("sync", "同步复制"), // master 复制给所有的slave
    HALF_SYNC("half_sync", "半同步复制"), // 如果有超过半数的slave复制成功, 那么master告诉客户端写入成功
    ASYNC("async", "异步复制"), // 数据写入到master之后, master的一个异步线程发送同步数据到slave
    ;

    private String code;

    private String desc;

    MasterSlaveReplicationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
