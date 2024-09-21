package com.wuyiccc.hellomq.broker.utils;

/**
 * @author wuyiccc
 * @date 2024/9/21 21:10
 */
public interface PutMessageLock {

    /**
     * 加锁
     */
    void lock();

    /**
     * 解锁
     */
    void unLock();
}
