package com.jeeagile.frame.service.system;


import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.entity.system.AgileSysUserRole;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysUserRoleService extends IAgileBaseService<AgileSysUserRole> {
    /**
     * 获取用户分配角色ID
     *
     * @param userId
     * @return
     */
    List<String> getUserRoleIdList(Serializable userId);
}
