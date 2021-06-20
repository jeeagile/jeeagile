package com.jeeagile.system.service;


import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysUserRole;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysUserRoleService extends IAgileBaseService<AgileSysUserRole> {
    /**
     * 查看用户已分配角色
     */
    List<AgileSysUserRole> selectListByUserId(String userId);

    /**
     * 根据用户ID删除用户角色关系表
     */
    boolean deleteByUserId(String userId);

    /**
     * 根据角色ID删除用户角色关系表
     */
    boolean deleteByRoleId(String roleId);

}
