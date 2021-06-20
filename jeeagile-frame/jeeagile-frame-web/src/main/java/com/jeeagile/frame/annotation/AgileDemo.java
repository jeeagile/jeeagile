package com.jeeagile.frame.annotation;

import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 演示模式注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AgileDemo {

}
