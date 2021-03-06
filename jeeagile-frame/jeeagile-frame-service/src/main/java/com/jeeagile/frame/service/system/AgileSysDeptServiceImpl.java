package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileTreeServiceImpl;
import com.jeeagile.frame.entity.system.AgileSysDept;
import com.jeeagile.frame.entity.system.AgileSysRoleDept;
import com.jeeagile.frame.mapper.system.AgileSysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysDeptServiceImpl extends AgileTreeServiceImpl<AgileSysDeptMapper, AgileSysDept> implements IAgileSysDeptService {

    @Autowired
    private IAgileSysRoleDeptService agileSysRoleDeptService;

    @Override
    public LambdaQueryWrapper<AgileSysDept> queryWrapper(AgileSysDept agileSysDept) {
        LambdaQueryWrapper<AgileSysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysDept != null) {
            if (AgileStringUtil.isNotEmpty(agileSysDept.getDeptCode())) {
                lambdaQueryWrapper.eq(AgileSysDept::getDeptCode, agileSysDept.getDeptCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysDept.getDeptName())) {
                lambdaQueryWrapper.like(AgileSysDept::getDeptName, agileSysDept.getDeptName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysDept.getDeptStatus())) {
                lambdaQueryWrapper.eq(AgileSysDept::getDeptStatus, agileSysDept.getDeptStatus());
            }
        }
        lambdaQueryWrapper.orderByAsc(AgileSysDept::getParentId, AgileSysDept::getDeptSort);
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileSysDept agileSysDept) {
        this.validateData(agileSysDept);
    }

    @Override
    public void updateModelValidate(AgileSysDept agileSysDept) {
        this.validateData(agileSysDept);
    }

    @Override
    public boolean deleteModel(Serializable deptId) {
        this.deleteModelValidate(deptId);
        this.deleteRoleDept(deptId);
        return this.removeById(deptId);
    }

    @Override
    public void deleteModelValidate(Serializable deptId) {
        if (this.hasChild(deptId)) {
            throw new AgileValidateException("??????????????????????????????????????????????????????");
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param deptId
     * @return
     */
    private boolean deleteRoleDept(Serializable deptId) {
        if (AgileStringUtil.isNotEmpty(deptId)) {
            LambdaQueryWrapper<AgileSysRoleDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysRoleDept::getDeptId, deptId);
            return agileSysRoleDeptService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * ?????????????????????????????????
     */
    private void validateData(AgileSysDept agileSysDept) {
        LambdaQueryWrapper<AgileSysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysDept.isNotEmptyPk()) {
            lambdaQueryWrapper.ne(AgileSysDept::getId, agileSysDept.getId());
        }
        lambdaQueryWrapper.and(wrapper ->
                wrapper.eq(AgileSysDept::getDeptCode, agileSysDept.getDeptCode())
                        .or().eq(AgileSysDept::getDeptName, agileSysDept.getDeptName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("???????????????????????????????????????");
        }
    }
}
