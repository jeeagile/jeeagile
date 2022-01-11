package com.jeeagile.core.protocol.annotation.rabbit;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description rabbit 服务提供方参数支持
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface RabbitService {

    @AliasFor("rabbitName")
    String value() default "";

    /**
     * 用来生成交换机名、队列名、路由名
     */
    @AliasFor("value")
    String rabbitName() default "";

    /**
     * 连接工厂名称
     */
    String connFactoryName() default "default";

    /**
     * 接收超时时间
     */
    long receiveTimeout() default 50000L;

    /**
     * 响应超时时间
     */
    long replyTimeout() default 50000L;

    /**
     * 消息的过期时间
     */
    long messageTtl() default 60000L;

    /**
     * 是否异步发送
     */
    boolean async() default false;

    /**
     * 线程数
     * @return
     */
    int taskThreadCount() default 0;

    /**
     * 并发消费者个数
     */
    int concurrentConsumers() default 50;

    /**
     * 最大消费者个数
     */
    int maxConcurrentConsumers() default 100;

    /**
     * 待消费消息个数
     */
    int prefetchCount() default 30;
}
