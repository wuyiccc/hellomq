package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.nameserver.event.model.Event;

/**
 * @author wuyiccc
 * @date 2024/10/5 23:02
 */
public interface Listener<E extends Event> {

    /**
     * 回调通知
     */
    void onReceive(E event) throws Exception;
}
