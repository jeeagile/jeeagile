package com.jeeagile.drools.properties;

import lombok.Data;

import java.util.List;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-16
 * @描述 自定义 drools 配置
 */
@Data
public class AgileKieBaseProperties {
    /**
     * 映射packages
     */
    private List<String> packages;
    /**
     * 规则文件路径
     */
    private List<String> paths;
    /**
     * 规则文件编码类型
     */
    private String charset;
}
