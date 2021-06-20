package com.jeeagile.core.util.system.info.oshi;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 内存信息
 */
@Data
public class MemoryInfo {

    /**
     * 内存大小
     */
    private String total;
    /**
     * 空闲内存
     */
    private String available;
    /**
     * 已使用内存
     */
    private String used;
    /**
     * 内存使用率
     */
    private String usageRate;

}
