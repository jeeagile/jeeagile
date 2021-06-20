package com.jeeagile.frame.support.resolver.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SingleRequestBody {
    /**
     * 字段名
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 字段名
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 是否必填
     *
     * @return
     */
    boolean required() default false;

    /**
     * 设置默认值
     *
     * @return
     */
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}

