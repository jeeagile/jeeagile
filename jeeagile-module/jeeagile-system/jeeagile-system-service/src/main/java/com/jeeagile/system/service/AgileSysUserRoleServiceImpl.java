package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysUserRole;
import com.jeeagile.system.mapper.AgileSysUserRoleMapper;

import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysUserRoleServiceImpl extends AgileBaseServiceImpl<AgileSysUserRoleMapper, AgileSysUserRole> implements IAgileSysUserRoleService {

    @Override
    public List<AgileSysUserRole> selectListByUserId(String userId) {
        QueryWrapper<AgileSysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysUserRole::getUserId, userId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteByUserId(String userId) {
        QueryWrapper<AgileSysUserRole> whereWrapper = new QueryWrapper<>();
        if (AgileStringUtil.isNotEmpty(userId)) {
            whereWrapper.lambda().eq(AgileSysUserRole::getUserId, userId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteByRoleId(String roleId) {
        QueryWrapper<AgileSysUserRole> whereWrapper = new QueryWrapper<>();
        if (AgileStringUtil.isNotEmpty(roleId)) {
            whereWrapper.lambda().eq(AgileSysUserRole::getRoleId, roleId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }
}
