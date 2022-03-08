package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseTreeServiceImpl;
import com.jeeagile.system.entity.AgileSysDept;
import com.jeeagile.system.mapper.AgileSysDeptMapper;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysDeptServiceImpl extends AgileBaseTreeServiceImpl<AgileSysDeptMapper, AgileSysDept> implements IAgileSysDeptService {

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
    public void deleteModelValidate(Serializable id) {
        if (this.countChild(id) > 0) {
            throw new AgileValidateException("该部门存在下级部门不能进行删除操作！");
        }
    }

    /**
     * 校验部门编码和部门名称
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
            throw new AgileValidateException("部门编码或部门名称已存在！");
        }
    }
}
