package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseTreeService;
import com.jeeagile.system.entity.AgileSysMenu;
import com.jeeagile.system.vo.AgileUpdateSort;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysMenuService extends IAgileBaseTreeService<AgileSysMenu> {
    /**
     * 获取菜单列表
     */
    List<AgileSysMenu> selectMenuList(AgileSysMenu agileSysMenu);

    /**
     * 查看菜单信息
     */
    AgileSysMenu selectMenuById(String menuId);

    /**
     * 保存菜单
     */
    AgileSysMenu saveMenu(AgileSysMenu agileSysMenu);

    /**
     * 修改菜单
     */
    boolean updateMenuById(AgileSysMenu agileSysMenu);

    /**
     * 修改菜单排序
     */
    boolean updateMenuSort(List<AgileUpdateSort> agileSortList);

    /**
     * 删除菜单
     */
    boolean deleteMenuById(String menuId);
}
