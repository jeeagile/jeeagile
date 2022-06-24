package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.enums.AgileAuditStatus;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysTenant;
import com.jeeagile.frame.mapper.system.AgileSysTenantMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

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
    public AgileSysTenant getAgileSysTenant(String tenantCode) {
        LambdaQueryWrapper<AgileSysTenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysTenant::getTenantCode, tenantCode);
        return this.getOne(lambdaQueryWrapper);
    }
}
