package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.entity.system.AgileSysUserRole;
import com.jeeagile.frame.mapper.system.AgileSysUserRoleMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysUserRoleServiceImpl extends AgileBaseServiceImpl<AgileSysUserRoleMapper, AgileSysUserRole> implements IAgileSysUserRoleService {

    @Override
    public List<String> getUserRoleIdList(Serializable userId) {
        List<String> userRoleIdList = new ArrayList<>();
        LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserRole::getUserId, userId);
        List<AgileSysUserRole> agileSysUserRoleList = this.list(lambdaQueryWrapper);
        agileSysUserRoleList.forEach(item -> userRoleIdList.add(item.getRoleId()));
        return userRoleIdList;
    }
}
