package com.jeeagile.core.util.system.info.oshi;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 交换区信息
 */
@Data
public class SwapInfo {
    /**
     * 交换区大小
     */
    private String total;
    /**
     * 空闲交换区
     */
    private String available;
    /**
     * 已使用交换区
     */
    private String used;
    /**
     * 交换区使用率
     */
    private String usageRate;
}
