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
     * 修改菜单排序
     */
    boolean updateMenuSort(List<AgileUpdateSort> agileSortList);

}
