package com.jeeagile.core.protocol.annotation;

import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Inherited
public @interface AgileMethod {
    /**
     * 方法名称
     */
    String name();

    /**
     * 是否异步发送
     */
    boolean async() default false;

    /**
     * 超时时间
     */
    int timeout() default -1;

}
