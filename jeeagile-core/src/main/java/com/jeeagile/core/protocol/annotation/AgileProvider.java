package com.jeeagile.core.protocol.annotation;


import com.jeeagile.core.protocol.annotation.dubbo.DubboProvider;
import com.jeeagile.core.protocol.annotation.rabbit.RabbitProvider;

import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 服务调用方注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AgileProvider {

    /**
     * 接口类型
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 接口名称
     */
    String interfaceName() default "";

    /**
     * 版本
     */
    String version() default "";

    /**
     * 是否异步发送
     */
    boolean async() default false;

    /**
     * rabbit配置
     */
    RabbitProvider rabbitProvider() default @RabbitProvider();

    /**
     * dubbo配置
     */
    DubboProvider dubboProvider() default @DubboProvider();

    /**
     * 方法配置
     * @return
     */
    AgileMethod[] method() default {};
}
