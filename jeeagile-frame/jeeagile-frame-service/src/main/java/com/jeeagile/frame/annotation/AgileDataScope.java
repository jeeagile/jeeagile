package com.jeeagile.frame.annotation;


import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2022-03-21
 * @description 数据权限注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AgileDataScope {

}
