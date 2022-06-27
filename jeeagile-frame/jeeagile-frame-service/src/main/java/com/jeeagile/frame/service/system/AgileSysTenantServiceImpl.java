package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.enums.AgileAuditStatus;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysTenant;
import com.jeeagile.frame.mapper.system.AgileSysTenantMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2022-06-24
 * @description 租户管理
 */
@AgileService
public class AgileSysTenantServiceImpl extends AgileBaseServiceImpl<AgileSysTenantMapper, AgileSysTenant> implements IAgileSysTenantService {
    @Override
    public LambdaQueryWrapper<AgileSysTenant> queryWrapper(AgileSysTenant agileSysTenant) {
        LambdaQueryWrapper<AgileSysTenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysTenant != null) {
            if (AgileStringUtil.isNotEmpty(agileSysTenant.getTenantName())) {
                lambdaQueryWrapper.like(AgileSysTenant::getTenantName, agileSysTenant.getTenantName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysTenant.getTenantCode())) {
                lambdaQueryWrapper.eq(AgileSysTenant::getTenantCode, agileSysTenant.getTenantCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysTenant.getEnableStatus())) {
                lambdaQueryWrapper.eq(AgileSysTenant::getEnableStatus, agileSysTenant.getEnableStatus());
            }
        }
        return lambdaQueryWrapper;
    }


    @Override
    public void saveModelValidate(AgileSysTenant agileSysTenant) {
        agileSysTenant.setAuditStatus(AgileAuditStatus.AUDIT.getCode());
        this.validateData(agileSysTenant);
    }

    @Override
    public void updateModelValidate(AgileSysTenant agileSysTenant) {
        this.validateData(agileSysTenant);
    }

    /**
     * 校验参数名称或参数键名不能与已存在的数据重复
     */
    private void validateData(AgileSysTenant agileSysTenant) {
        LambdaQueryWrapper<AgileSysTenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysTenant.getId() != null) {
            lambdaQueryWrapper.ne(AgileSysTenant::getId, agileSysTenant.getId());
        }
        lambdaQueryWrapper.and(wrapper ->
                wrapper.eq(AgileSysTenant::getTenantCode, agileSysTenant.getTenantCode()).or().eq(AgileSysTenant::getTenantName, agileSysTenant.getTenantName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("租户编码或租户名称已存在！");
        }
    }


    @Override
    public boolean audit(AgileSysTenant agileSysTenant) {
        AgileSysTenant agileSysTenantOld = this.getById(agileSysTenant.getId());
        if (agileSysTenantOld == null || agileSysTenantOld.isEmptyPk()) {
            throw new AgileFrameException("租户信息已不存在！");
        }
        if (agileSysTenant.getAuditStatus().equals("0")) {
            this.initTenantInfo(agileSysTenantOld);
        }
        return this.updateModel(agileSysTenant);
    }

    @Override
    public void deleteModelValidate(Serializable id) {
        AgileSysTenant agileSysTenant = this.getById(id);
        if (agileSysTenant == null || agileSysTenant.isEmptyPk()) {
            throw new AgileFrameException("租户信息已不存在！");
        }
        this.deleteTenantInfo(agileSysTenant);
    }

    /**
     * 删除租户信息
     *
     * @param agileSysTenant
     */
    private void deleteTenantInfo(AgileSysTenant agileSysTenant) {

    }


    /**
     * 初始化租户基础信息
     *
     * @param agileSysTenant
     */
    private void initTenantInfo(AgileSysTenant agileSysTenant) {

    }

}
