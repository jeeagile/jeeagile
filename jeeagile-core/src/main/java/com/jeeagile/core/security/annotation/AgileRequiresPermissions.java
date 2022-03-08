package com.jeeagile.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 自定义权限注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgileRequiresPermissions {
    /**
     * 权限值
     *
     * @return
     */
    String[] value();

    /**
     * 是否启用获取前缀
     *
     * @return
     */
    boolean prefix() default true;

    /**
     * 逻辑关系
     *
     * @return
     */
    AgileLogical logical() default AgileLogical.AND;
}
