package com.jeeagile.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 自定义权限角色注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgileRequiresRoles {
    /**
     * 角色值
     *
     * @return
     */
    String[] value();

    /**
     * 逻辑关系
     *
     * @return
     */
    AgileLogical logical() default AgileLogical.AND;
}
