package com.jeeagile.core.util.system.info;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description java jvm信息
 */
@Data
public class JvmInfo {
    /**
     * VM名称
     */
    private String name;
    /**
     * VM版本
     */
    private String version;
    /**
     * VM厂商
     */
    private String vendor;
    /**
     * VM信息
     */
    private String info;

    /**
     * 启动参数
     */
    private String startArgs;

    /**
     * 启动时间
     */
    private String startTime;
    /**
     * 运行时间
     */
    private String runTime;
}
