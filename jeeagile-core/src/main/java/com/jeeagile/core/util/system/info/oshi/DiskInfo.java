package com.jeeagile.core.util.system.info.oshi;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 磁盘信息
 */
@Data
public class DiskInfo {
    /**
     * 磁盘盘符路径
     */
    private String fileMount;
    /**
     * 磁盘文件类型
     */
    private String fileType;
    /**
     * 磁盘文件描述
     */
    private String fileDesc;
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
