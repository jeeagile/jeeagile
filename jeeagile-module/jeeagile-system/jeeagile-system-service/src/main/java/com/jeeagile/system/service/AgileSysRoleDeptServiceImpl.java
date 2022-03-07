package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysRoleDept;
import com.jeeagile.system.mapper.AgileSysRoleDeptMapper;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysRoleDeptServiceImpl extends AgileBaseServiceImpl<AgileSysRoleDeptMapper, AgileSysRoleDept> implements IAgileSysRoleDeptService {

    @Override
    public List<AgileSysRoleDept> selectListByRoleId(String roleId) {
        QueryWrapper<AgileSysRoleDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysRoleDept::getRoleId, roleId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteByRoleId(String roleId) {
        if (AgileStringUtil.isNotEmpty(roleId)) {
            QueryWrapper<AgileSysRoleDept> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(AgileSysRoleDept::getRoleId, roleId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteByDeptId(String deptId) {
        if (AgileStringUtil.isNotEmpty(deptId)) {
            QueryWrapper<AgileSysRoleDept> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(AgileSysRoleDept::getDeptId, deptId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }
}
