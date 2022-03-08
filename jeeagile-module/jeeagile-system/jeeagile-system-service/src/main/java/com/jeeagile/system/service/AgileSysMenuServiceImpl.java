package com.jeeagile.system.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseTreeServiceImpl;
import com.jeeagile.system.entity.AgileSysMenu;
import com.jeeagile.system.mapper.AgileSysMenuMapper;
import com.jeeagile.system.vo.AgileUpdateSort;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysMenuServiceImpl extends AgileBaseTreeServiceImpl<AgileSysMenuMapper, AgileSysMenu> implements IAgileSysMenuService {

    @Autowired
    private IAgileSysRoleMenuService agileSysRoleMenuService;

    @Override
    public LambdaQueryWrapper<AgileSysMenu> queryWrapper(AgileSysMenu agileSysMenu) {
        LambdaQueryWrapper<AgileSysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysMenu != null) {
            if (AgileStringUtil.isNotEmpty(agileSysMenu.getMenuName())) {
                lambdaQueryWrapper.like(AgileSysMenu::getMenuName, agileSysMenu.getMenuName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysMenu.getMenuStatus())) {
                lambdaQueryWrapper.eq(AgileSysMenu::getMenuStatus, agileSysMenu.getMenuStatus());
            }
        }
        lambdaQueryWrapper.orderByAsc(AgileSysMenu::getParentId, AgileSysMenu::getMenuSort);
        return lambdaQueryWrapper;
    }

    @Override
    public boolean updateMenuSort(List<AgileUpdateSort> agileSortList) {
        List<AgileSysMenu> agileSysMenuList = new ArrayList<>();
        for (AgileUpdateSort agileUpdateSort : agileSortList) {
            AgileSysMenu agileSysMenu = new AgileSysMenu();
            agileSysMenu.setId(agileUpdateSort.getId());
            agileSysMenu.setMenuSort(agileUpdateSort.getSort());
            agileSysMenuList.add(agileSysMenu);
        }
        return this.updateBatchById(agileSysMenuList);
    }

    @Override
    public boolean deleteModel(Serializable id) {
        this.deleteModelValidate(id);
        agileSysRoleMenuService.deleteByMenuId((String) id);
        return super.deleteModel(id);
    }

    @Override
    public void deleteModelValidate(Serializable id) {
        if (this.countChild(id) > 0) {
            throw new AgileValidateException("该菜单下存在下级菜单不能进行删除操作！");
        }
    }
}
