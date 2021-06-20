package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.validate.ValidateUtil;
import com.jeeagile.frame.service.AgileBaseTreeServiceImpl;
import com.jeeagile.system.entity.AgileSysDept;
import com.jeeagile.system.mapper.AgileSysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysDeptServiceImpl extends AgileBaseTreeServiceImpl<AgileSysDeptMapper, AgileSysDept> implements IAgileSysDeptService {

    @Autowired
    private IAgileSysRoleDeptService agileSysRoleDeptService;

    @Override
    public List<AgileSysDept> selectDeptList(AgileSysDept agileSysDept) {
        return this.list(getSysDeptQueryWrapper(agileSysDept));
    }

    @Override
    public AgileSysDept selectDeptById(String deptId) {
        return this.getById(deptId);
    }

    @Override
    public String getDeptNameById(String deptId) {
        if (StringUtil.isNotEmpty(deptId)) {
            AgileSysDept agileSysDept = this.getById(deptId);
            if (agileSysDept != null) {
                return agileSysDept.getDeptName();
            }
        }
        return null;
    }

    @Override
    public AgileSysDept saveDept(AgileSysDept agileSysDept) {
        //校验业务数据
        ValidateUtil.validateObject(agileSysDept);
        //保存部门信息
        this.save(agileSysDept);
        return agileSysDept;
    }

    @Override
    public boolean updateDeptById(AgileSysDept agileSysDept) {
        //校验业务数据
        ValidateUtil.validateObject(agileSysDept);
        return this.updateById(agileSysDept);
    }

    @Override
    public boolean deleteDeptById(String deptId) {
        if (this.countChild(deptId) > 0) {
            throw new AgileValidateException("该部门存在下级部门不能进行删除操作！");
        }
        agileSysRoleDeptService.deleteByDeptId(deptId);
        return this.removeById(deptId);
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysDept> getSysDeptQueryWrapper(AgileSysDept agileSysDept) {
        QueryWrapper<AgileSysDept> queryWrapper = new QueryWrapper<>();
        if (agileSysDept != null) {
            if (StringUtil.isNotEmpty(agileSysDept.getDeptCode())) {
                queryWrapper.lambda().eq(AgileSysDept::getDeptCode, agileSysDept.getDeptCode());
            }
            if (StringUtil.isNotEmpty(agileSysDept.getDeptName())) {
                queryWrapper.lambda().like(AgileSysDept::getDeptName, agileSysDept.getDeptName());
            }
            if (StringUtil.isNotEmpty(agileSysDept.getDeptStatus())) {
                queryWrapper.lambda().eq(AgileSysDept::getDeptStatus, agileSysDept.getDeptStatus());
            }
        }
        queryWrapper.lambda().orderByAsc(AgileSysDept::getParentId, AgileSysDept::getDeptSort);
        return queryWrapper;
    }
}
