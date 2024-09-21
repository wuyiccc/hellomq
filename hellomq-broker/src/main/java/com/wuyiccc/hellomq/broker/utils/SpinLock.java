package com.wuyiccc.hellomq.broker.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuyiccc
 * @date 2024/9/21 21:22
 */
public class SpinLock implements PutMessageLock {
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void lock() {

        do {
            int result = atomicInteger.getAndIncrement();
            if (result == 1) {
                return;
            }
        } while (true);

    }

    @Override
    public void unLock() {

        atomicInteger.decrementAndGet();

    }
}
