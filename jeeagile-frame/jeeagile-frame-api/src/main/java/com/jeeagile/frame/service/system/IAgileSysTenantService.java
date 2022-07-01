package com.jeeagile.frame.service.system;

import com.jeeagile.frame.entity.system.AgileSysTenant;
import com.jeeagile.frame.service.IAgileBaseService;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2022-03-22
 * @description 租户管理
 */
public interface IAgileSysTenantService extends IAgileBaseService<AgileSysTenant> {

    /**
     * 获取租户信息 非登录模式使用需禁用租户查询条件
     * @param tenantId
     * @return
     */
    AgileSysTenant agileSysTenantInfo(Serializable tenantId);
    /**
     * 审核
     * @param agileSysTenant
     * @return
     */
    boolean audit(AgileSysTenant agileSysTenant);
}
