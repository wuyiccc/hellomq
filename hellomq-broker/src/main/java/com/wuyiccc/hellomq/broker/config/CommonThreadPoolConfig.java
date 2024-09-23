package com.wuyiccc.hellomq.broker.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyiccc
 * @date 2024/9/21 10:36
 * <p>
 * 通用线程池配置
 */
public class CommonThreadPoolConfig {

    // 专门用于将topic配置信息异步刷盘的线程池
    public static ThreadPoolExecutor refreshHelloMqTopicExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("refresh-hello-mq-topic-config");
                return thread;
            });

    // 专门用于将各个消费者的消费进度持久化到磁盘中
    public static ThreadPoolExecutor refreshConsumeQueueOffsetExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("refresh-hello-mq-topic-config");
                return thread;
            });




}
