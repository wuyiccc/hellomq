package com.wuyiccc.hellomq.broker.utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuyiccc
 * @date 2024/9/21 21:20
 */
public class UnfairReentrantLock implements PutMessageLock {

    private ReentrantLock reentrantLock = new ReentrantLock();
    @Override
    public void lock() {

        reentrantLock.lock();
    }

    @Override
    public void unLock() {


        reentrantLock.unlock();
    }
}
