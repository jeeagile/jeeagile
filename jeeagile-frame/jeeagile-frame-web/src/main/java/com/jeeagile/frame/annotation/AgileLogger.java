package com.jeeagile.frame.annotation;

import com.jeeagile.frame.enums.AgileLoggerType;
import lombok.NonNull;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface AgileLogger {
    /**
     * 模块
     *
     * @return
     */
    @AliasFor("module")
    String value() default "";
    /**
     * 模块
     *
     * @return
     */
    @AliasFor("value")
    String module() default "";

    /**
     * 描述
     *
     * @return
     */
    String notes() default "";

    /**
     * 业务操作类型
     *
     * @return
     */
    @NonNull
    AgileLoggerType type() default AgileLoggerType.OTHER;

    /**
     * 是否保存请求和返回数据
     * 默认不记录数据
     *
     * @return
     */
    boolean param() default false;
}
