package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileTreeServiceImpl;
import com.jeeagile.frame.entity.system.AgileSysMenu;
import com.jeeagile.frame.entity.system.AgileSysRoleMenu;
import com.jeeagile.frame.mapper.system.AgileSysMenuMapper;
import com.jeeagile.frame.vo.system.AgileMenuSort;
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
public class AgileSysMenuServiceImpl extends AgileTreeServiceImpl<AgileSysMenuMapper, AgileSysMenu> implements IAgileSysMenuService {

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
    public boolean updateMenuSort(List<AgileMenuSort> agileMenuSortList) {
        List<AgileSysMenu> agileSysMenuList = new ArrayList<>();
        for (AgileMenuSort agileMenuSort : agileMenuSortList) {
            AgileSysMenu agileSysMenu = new AgileSysMenu();
            agileSysMenu.setId(agileMenuSort.getId());
            agileSysMenu.setMenuSort(agileMenuSort.getMenuSort());
            agileSysMenuList.add(agileSysMenu);
        }
        return this.updateBatchById(agileSysMenuList);
    }

    @Override
    public boolean deleteModel(Serializable menuId) {
        this.deleteModelValidate(menuId);
        this.deleteRoleMenu(menuId);
        return this.removeById(menuId);
    }

    @Override
    public void deleteModelValidate(Serializable menuId) {
        if (this.hasChild(menuId)) {
            throw new AgileValidateException("该菜单下存在下级菜单不能进行删除操作！");
        }
    }

    /**
     * 删除该菜单从已判定的角色中删除
     *
     * @param menuId
     * @return
     */
    private boolean deleteRoleMenu(Serializable menuId) {
        if (AgileStringUtil.isNotEmpty(menuId)) {
            LambdaQueryWrapper<AgileSysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysRoleMenu::getMenuId, menuId);
            return agileSysRoleMenuService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }
}
