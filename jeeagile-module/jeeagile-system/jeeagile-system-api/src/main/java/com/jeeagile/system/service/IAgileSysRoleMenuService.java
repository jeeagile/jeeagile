package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysRoleMenu;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysRoleMenuService extends IAgileBaseService<AgileSysRoleMenu> {

    /**
     * 根据角色id获取已分配的菜单
     */
    List<AgileSysRoleMenu> selectListByRoleId(String roleId) ;

    /**
     * 根据角色ID删除角色菜单
     */
    boolean deleteByRoleId(String roleId) ;

    /**
     * 根据菜单ID删除角色菜单
     *
     * @param menuId
     * @return
     */
    boolean deleteByMenuId(String menuId);
}
