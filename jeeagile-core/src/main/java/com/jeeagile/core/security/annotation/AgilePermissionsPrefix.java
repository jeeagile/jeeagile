package com.jeeagile.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 自定义权限注解 权限前缀
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgilePermissionsPrefix {
    /**
     * 权限前缀值
     *
     * @return
     */
    String value();
}
