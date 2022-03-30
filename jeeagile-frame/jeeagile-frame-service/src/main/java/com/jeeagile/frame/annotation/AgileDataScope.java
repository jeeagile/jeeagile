package com.jeeagile.frame.annotation;


import java.lang.annotation.*;

/**
 * @author JeeAgile
 * @date 2022-03-21
 * @description 数据权限注解
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AgileDataScope {
    /**
     * 数据权限类型 默认 系统内部实现 方便 二次开发使用
     *
     * @return
     */
    String type() default "inner";

    /**
     * 内部权限 仅本人数据权限 字段设置
     *
     * @return
     */
    AgileDataColumn user() default @AgileDataColumn(name = "create_user");

    /**
     * 内部权限  部门权限 字段设置
     *
     * @return
     */
    AgileDataColumn dept() default @AgileDataColumn(name = "dept_id");

    /**
     * 其他字段权限配置
     *
     * @return
     */
    AgileDataColumn[] other() default {};
}
