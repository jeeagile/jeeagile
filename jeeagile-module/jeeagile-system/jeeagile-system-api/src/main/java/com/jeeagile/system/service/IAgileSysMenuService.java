package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileTreeService;
import com.jeeagile.system.entity.AgileSysMenu;
import com.jeeagile.system.vo.AgileMenuSort;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysMenuService extends IAgileTreeService<AgileSysMenu> {

    /**
     * 修改菜单排序
     */
    boolean updateMenuSort(List<AgileMenuSort> agileMenuSortList);

}
