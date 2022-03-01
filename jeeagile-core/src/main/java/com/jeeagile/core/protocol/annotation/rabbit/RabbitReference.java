package com.jeeagile.core.protocol.annotation.rabbit;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description rabbit 服务调用方参数支持
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface RabbitReference {
    /**
     * 用来生成交换机名、队列名、路由名
     */
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
     * 线程数
     * @return
     */
    int taskThreadCount() default 0;

    /**
     * 消息是否进行持久化
     * @return
     */
    boolean messageDelivery() default false;
}
