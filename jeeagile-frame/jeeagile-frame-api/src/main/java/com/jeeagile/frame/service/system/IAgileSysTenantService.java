package com.jeeagile.frame.service.system;

import com.jeeagile.frame.entity.system.AgileSysTenant;
import com.jeeagile.frame.service.IAgileBaseService;

/**
 * @author JeeAgile
 * @date 2022-03-22
 * @description 租户管理
 */
public interface IAgileSysTenantService extends IAgileBaseService<AgileSysTenant> {
    /**
     * 审核
     * @param agileSysTenant
     * @return
     */
    boolean audit(AgileSysTenant agileSysTenant);
}
