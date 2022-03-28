package com.jeeagile.frame.annotation;

import lombok.NonNull;

public @interface AgileDataColumn {
    /**
     * 字段别名
     *
     * @return
     */
    String alias() default "";

    /**
     * 字段名称
     *
     * @return
     */
    @NonNull
    String name();
}
