package com.jeeagile.system.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import com.jeeagile.frame.service.AgileBaseTreeServiceImpl;
import com.jeeagile.system.entity.AgileSysMenu;
import com.jeeagile.system.mapper.AgileSysMenuMapper;
import com.jeeagile.system.vo.AgileUpdateSort;
import org.springframework.beans.factory.annotation.Autowired;

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
    public List<AgileSysMenu> selectMenuList(AgileSysMenu agileSysMenu) {
        return this.list(getSysMenuQueryWrapper(agileSysMenu));
    }

    @Override
    public AgileSysMenu selectMenuById(String menuId) {
        return this.getById(menuId);
    }

    @Override
    public AgileSysMenu saveMenu(AgileSysMenu agileSysMenu) {
        //校验业务数据
        AgileValidateUtil.validateObject(agileSysMenu);
        //保存菜单信息
        this.save(agileSysMenu);
        return agileSysMenu;
    }

    @Override
    public boolean updateMenuById(AgileSysMenu agileSysMenu) {
        //校验业务数据
        AgileValidateUtil.validateObject(agileSysMenu);
        return this.updateById(agileSysMenu);
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
    public boolean deleteMenuById(String menuId) {
        if (this.countChild(menuId) > 0) {
            throw new AgileValidateException("该菜单下存在下级菜单不能进行删除操作！");
        }
        agileSysRoleMenuService.deleteByMenuId(menuId);
        return this.removeById(menuId);
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysMenu> getSysMenuQueryWrapper(AgileSysMenu agileSysMenu) {
        QueryWrapper<AgileSysMenu> queryWrapper = new QueryWrapper<>();
        if (agileSysMenu != null) {
            if (StringUtil.isNotEmpty(agileSysMenu.getMenuName())) {
                queryWrapper.lambda().like(AgileSysMenu::getMenuName, agileSysMenu.getMenuName());
            }
            if (StringUtil.isNotEmpty(agileSysMenu.getMenuStatus())) {
                queryWrapper.lambda().eq(AgileSysMenu::getMenuStatus, agileSysMenu.getMenuStatus());
            }
        }
        queryWrapper.lambda().orderByAsc(AgileSysMenu::getParentId, AgileSysMenu::getMenuSort);
        return queryWrapper;
    }
}
