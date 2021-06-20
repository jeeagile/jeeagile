package com.jeeagile.system.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.vo.AgileSysRoleInfo;
import com.jeeagile.system.vo.AgileUpdateStatus;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysRoleService extends IAgileBaseService<AgileSysRole> {

    /**
     * 分页查询角色列表
     */
    AgilePage<AgileSysRole> selectRolePage(AgilePageable<AgileSysRole> agilePageable) ;

    /**
     * 获取角色列表
     */
    List<AgileSysRole> selectRoleList(AgileSysRole agileSysRole) ;

    /**
     * 查看角色信息
     */
    AgileSysRole selectRoleById(String roleId) ;

    /**
     * 保存角色信息
     */
    AgileSysRole saveRole(AgileSysRoleInfo agileSysRole) ;

    /**
     * 修改角色信息
     */
    boolean updateRoleById(AgileSysRoleInfo agileSysRole) ;


    /**
     * 删除角色信息
     */
    boolean deleteRoleById(String roleId) ;

    /**
     * 修改角色状态
     */
    boolean changeRoleStatus(AgileUpdateStatus agileUpdateStatus);

    /**
     * 修改角色数据范围
     */
    boolean updateRoleDataScope(AgileSysRoleInfo agileSysRoleInfo) ;
}
