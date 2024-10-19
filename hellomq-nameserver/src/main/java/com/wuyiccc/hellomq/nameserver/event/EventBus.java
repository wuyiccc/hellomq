package com.wuyiccc.hellomq.nameserver.event;

import com.google.common.collect.Lists;
import com.wuyiccc.hellomq.common.constants.StrConstants;
import com.wuyiccc.hellomq.common.utils.ReflectUtils;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import com.wuyiccc.hellomq.nameserver.event.spi.listener.Listener;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyiccc
 * @date 2024/10/5 23:01
 */
public class EventBus {

    private static final Logger log = LoggerFactory.getLogger(EventBus.class);


    private Map<Class<? extends Event>, List<Listener>> eventListenerMap = new ConcurrentHashMap<>();

    private String taskName;

    public EventBus(String taskName) {
        log.info("初始化: {}", taskName);
        this.taskName = taskName;
    }

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10
            , 100
            , 3
            , TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(1000)
            , r -> {
        Thread thread = new Thread(r);
        thread.setName(taskName + StrConstants.DASHED + UUID.randomUUID().toString());
        return thread;
    });

    public void init() {

        log.info("eventBus init");
        ServiceLoader<Listener> serviceLoader = ServiceLoader.load(Listener.class);
        for (Listener listener : serviceLoader) {
            Class clazz = ReflectUtils.getInterfaceT(listener, 0);
            registry(clazz, listener);
        }
    }

    private <E extends Event> void registry(Class<? extends Event> clazz, Listener<E> listener) {

        List<Listener> listeners = eventListenerMap.get(clazz);

        if (CollectionUtils.isEmpty(listeners)) {
            eventListenerMap.put(clazz, Lists.newArrayList(listener));
        } else {
            listeners.add(listener);
            eventListenerMap.put(clazz, listeners);
        }
    }

    public void publish(Event event) {

        List<Listener> listeners = eventListenerMap.get(event.getClass());

        threadPoolExecutor.execute(() -> {
            try {
                for (Listener listener : listeners) {
                    listener.onReceive(event);
                }
            } catch (Exception e) {
                log.error("EventBus#publish执行异常", e);
            }
        });
    }
}
