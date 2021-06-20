package com.jeeagile.core.protocol.annotation;

import com.jeeagile.core.protocol.annotation.dubbo.DubboService;
import com.jeeagile.core.protocol.annotation.rabbit.RabbitService;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 服务提供方注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface AgileService {

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
     * rabbit 配置
     */
    RabbitService rabbitService() default @RabbitService();

    /**
     * dubbo 配置
     */
    DubboService dubboService() default @DubboService();

    /**
     * 方法配置
     */
    AgileMethod[] method() default {};
}
