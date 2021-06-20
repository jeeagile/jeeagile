package com.jeeagile.frame.annotation;

import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 自定义MAPPER 用于自动扫描使用
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AgileMapperScan {

}
