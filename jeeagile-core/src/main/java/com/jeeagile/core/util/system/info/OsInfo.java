package com.jeeagile.core.util.system.info;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description OS信息
 */
@Data
public class OsInfo {
    /**
     * OS架构
     */
    private String arch;
    /**
     * OS名称
     */
    private String name;
    /**
     * OS版本
     */
    private String version;
    /**
     * OS文件路径分隔符
     */
    private String fileSeparator;
    /**
     * OS文本文件换行符
     */
    private String lineSeparator;
    /**
     * OS搜索路径分隔符
     */
    private String pathSeparator;
}
