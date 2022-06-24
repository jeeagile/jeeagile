package com.jeeagile.core.util.tenant;

import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.properties.AgileTenantProperties;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.spring.AgileServletUtil;
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

    /**
     * 校验租户
     */
    public static void checkAgileTenant() {
        if (isTenantEnable()) {
            String tenantId = AgileServletUtil.getHeaderValue("AGILE_TENANT");
            String tenantSign = AgileServletUtil.getHeaderValue("AGILE_TENANT_SIGN");
            if (AgileStringUtil.isEmpty(tenantId) || AgileStringUtil.isEmpty(tenantSign)) {
                throw new AgileFrameException("非法访问！！！");
            }
        }
    }
}
