package com.jeeagile.frame.service.system;

import com.jeeagile.frame.service.IAgileTreeService;
import com.jeeagile.frame.entity.system.AgileSysMenu;
import com.jeeagile.frame.vo.system.AgileMenuSort;


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
