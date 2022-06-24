package com.jeeagile.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * @author JeeAgile
 * @date 2022-06-24
 * @description 租户配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "agile.tenant")
public class AgileTenantProperties implements Serializable {
    /**
     * 租户模式
     */
    private boolean enable = false;

    /**
     * 默认租户
     */
    private String defaultTenant = "jeeagile";

    /**
     * 需要进行租户配置表名，表必须添加字段 TENANT_ID
     * 已继承AgileTenantModel的无需进行配置，系统会自动识别
     */
    private Set<String> tables;
}
