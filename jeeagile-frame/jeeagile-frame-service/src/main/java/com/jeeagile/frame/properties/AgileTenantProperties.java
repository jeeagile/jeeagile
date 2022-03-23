package com.jeeagile.frame.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "agile.tenant")
public class AgileTenantProperties {
    /**
     * 租户模式
     */
    private boolean enable = false;

    /**
     * 需要进行租户配置表名，表必须添加字段 TENANT_ID
     * 已继承AgileTenantModel的无需进行配置，系统会自动识别
     */
    private Set<String> tables;
}
