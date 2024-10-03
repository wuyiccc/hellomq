package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.cache.CommonCache;

/**
 * @author wuyiccc
 * @date 2024/10/3 17:10
 * 消费队列消费处理器
 */
public class ConsumeQueueConsumeHandler {

    /**
     * 读取当前最新一条consumeQueue的消息内容
     */
    public byte[] consume(String topic, String consumeGroup, Integer queueId) {

        // 检查参数合法性
        // 获取当前匹配的对了最新的consumeQueue的offset是多少
        // 获取当前匹配队列的mmap对象
        CommonCache.getConsumeQueueOffsetModel();
        return null;
    }


    /**
     * 更新consumeQueue-offset的值
     */
    public boolean ack() {

        return true;
    }
}
