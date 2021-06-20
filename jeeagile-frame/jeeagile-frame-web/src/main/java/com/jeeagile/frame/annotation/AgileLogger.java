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
@Target({ElementType.METHOD})
@Documented
public @interface AgileLogger {
    /**
     * 日志标题
     *
     * @return
     */
    @AliasFor("title")
    String value() default "";

    /**
     * 日志标题
     *
     * @return
     */
    @NonNull
    @AliasFor("value")
    String title() default "";


    /**
     * 业务操作类型
     *
     * @return
     */
    AgileLoggerType type() default AgileLoggerType.OTHER;

    /**
     * 是否保存请求和返回数据
     * 默认不记录数据
     *
     * @return
     */
    boolean saveParam() default false;
}
