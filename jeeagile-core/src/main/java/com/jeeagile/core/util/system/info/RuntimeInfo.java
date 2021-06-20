package com.jeeagile.core.util.system.info;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 运行时信息
 */
@Data
public class RuntimeInfo {
    /**
     * JVM最大内存
     */
    private String maxMemory;
    /**
     * JVM已分配内存
     */
    private String totalMemory;
    /**
     * JVM空闲内存
     */
    private String freeMemory;
    /**
     * JVM可用内存
     */
    private String usableMemory;
}
