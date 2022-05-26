package com.jeeagile.frame.service.system;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.entity.system.AgileSysRole;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysRoleService extends IAgileBaseService<AgileSysRole> {
    /**
     * 修改角色状态
     */
    boolean changeRoleStatus(Serializable userId, String roleStatus);

    /**
     * 修改角色数据范围
     */
    boolean updateRoleDataScope(AgileSysRole agileSysRole);
}
