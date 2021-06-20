package com.jeeagile.core.util.system.info;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description Java信息
 */
@Data
public class JavaInfo {
    /**
     * Java版本
     */
    private String version;
    /**
     * Java厂商
     */
    private String vendor;
    /**
     * Java厂商网站的URL
     */
    private String vendorUrl;

    /**
     * JRE版本
     */
    private String runtimeVersion;
    /**
     * JRE名称
     */
    private String runtimeName;
    /**
     * JRE安装目录
     */
    private String homeDir;

    /**
     * JRE扩展目录列表
     */
    private String extDirs;

    /**
     * JRE系统环境变量
     */
    private String classPath;

    /**
     * JRE CLASS文件格式版本
     */
    private String classVersion;

    /**
     * JRE LIBRARY搜索路径
     */
    private String libraryPath;


}
