package com.jeeagile.core.util.system.info.oshi;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description CPU相关信息
 */
@Data
public class CpuInfo {
    /**
     * CPU名称
     */
    private String cpuName;
    /**
     * 物理CPU个数
     */
    private Integer packageNum;
    /**
     * 核心CPU个数
     */
    private Integer coreNum;
    /**
     * 逻辑CPU个数
     */
    private Integer logicNum;
    /**
     * CPU总的使用率
     */
    private double toTal;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;

    /**
     * CPU型号信息
     */
    private String cpuModel;
}
