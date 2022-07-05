package com.jeeagile.core.util.tenant;

import com.jeeagile.core.properties.AgileTenantProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 租户工具类
 */
@Component
public class AgileTenantUtil {
    private static AgileTenantProperties agileTenantProperties;

    private AgileTenantUtil(AgileTenantProperties agileTenantProperties) {
        this.agileTenantProperties = agileTenantProperties;
    }

    /**
     * 获取系统配置文件
     *
     * @return
     */
    public static AgileTenantProperties getAgileTenantProperties() {
        if (AgileTenantUtil.agileTenantProperties == null) {
            AgileTenantUtil.agileTenantProperties = new AgileTenantProperties();
        }
        return AgileTenantUtil.agileTenantProperties;
    }

    /**
     * 租户是否启用
     *
     * @return
     */
    public static boolean isTenantEnable() {
        return getAgileTenantProperties().isEnable();
    }

    /**
     * 默认租户
     * @return
     */
    public static String getDefaultTenant() {
        return getAgileTenantProperties().getDefaultTenant();
    }

    /**
     * 租户配置表
     *
     * @return
     */
    public static Set<String> getTenantTables() {
        return getAgileTenantProperties().getTables();
    }
}
