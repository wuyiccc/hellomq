package com.wuyiccc.hellomq.nameserver.event;

import com.google.common.collect.Lists;
import com.wuyiccc.hellomq.nameserver.event.listener.Listener;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuyiccc
 * @date 2024/10/5 23:01
 */
public class EventBus {

    private static Map<Class<? extends Event>, List<Listener>> eventListenerMap = new ConcurrentHashMap<>();

    public void init() {

        //registry();
    }

    private <E extends Event> void registry(Class<? extends Event> clazz, Listener<E> listener) {

        List<Listener> listeners = eventListenerMap.get(clazz);

        if (CollectionUtils.isEmpty(listeners)) {
            eventListenerMap.put(clazz, Lists.newArrayList(listeners));
        } else {
            listeners.add(listener);
            eventListenerMap.put(clazz, listeners);
        }
    }

    public void publish(Event event) {

        List<Listener> listeners = eventListenerMap.get(event.getClass());
        for (Listener listener : listeners) {
            listener.onReceive(event);
        }
    }
}
