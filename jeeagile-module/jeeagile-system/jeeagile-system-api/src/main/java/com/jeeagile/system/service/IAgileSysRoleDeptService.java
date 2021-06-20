package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysRoleDept;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysRoleDeptService extends IAgileBaseService<AgileSysRoleDept> {

    /**
     * 根据角色id获取已分配的部门权限
     */
    List<AgileSysRoleDept> selectListByRoleId(String roleId) ;

    /**
     * 根据角色ID删除已分配部门权限
     */
    boolean deleteByRoleId(String roleId) ;

    /**
     * 根据部门ID删除已分配部门权限
     */
    boolean deleteByDeptId(String deptId);
}
