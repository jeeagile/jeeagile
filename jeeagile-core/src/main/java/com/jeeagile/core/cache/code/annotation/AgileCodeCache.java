package com.jeeagile.core.cache.code.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 代码缓存注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AgileCodeCache {

    /**
     * 代码缓存KEY值
     * @return
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 代码缓存KEY值
     * @return
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 代码值字段
     *
     * @return
     */
    String[] optionCodeField() default {};

    /**
     * 代码名称字段
     *
     * @return
     */
    String[] optionNameField() default {};
}
