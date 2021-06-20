package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysRoleMenu;
import com.jeeagile.system.mapper.AgileSysRoleMenuMapper;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysRoleMenuServiceImpl extends AgileBaseServiceImpl<AgileSysRoleMenuMapper, AgileSysRoleMenu> implements IAgileSysRoleMenuService {

    @Override
    public List<AgileSysRoleMenu> selectListByRoleId(String roleId) {
        QueryWrapper<AgileSysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysRoleMenu::getRoleId, roleId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteByRoleId(String roleId) {
        if (StringUtil.isNotEmpty(roleId)) {
            QueryWrapper<AgileSysRoleMenu> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(AgileSysRoleMenu::getRoleId, roleId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteByMenuId(String menuId) {
        if (StringUtil.isNotEmpty(menuId)) {
            QueryWrapper<AgileSysRoleMenu> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(AgileSysRoleMenu::getMenuId, menuId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }
}
