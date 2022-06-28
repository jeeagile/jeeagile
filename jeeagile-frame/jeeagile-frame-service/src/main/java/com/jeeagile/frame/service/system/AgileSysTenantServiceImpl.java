package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.enums.AgileAuditStatus;
import com.jeeagile.core.enums.AgileUserStatus;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.tenant.AgileTenantUtil;
import com.jeeagile.frame.entity.system.AgileSysTenant;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.mapper.system.AgileSysTenantMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2022-06-24
 * @description 租户管理
 */
@AgileService
public class AgileSysTenantServiceImpl extends AgileBaseServiceImpl<AgileSysTenantMapper, AgileSysTenant> implements IAgileSysTenantService {
    @Autowired
    private IAgileSysUserService agileSysUserService;

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
        lambdaQueryWrapper.ne(AgileSysTenant::getTenantCode, AgileTenantUtil.getDefaultTenant());
        return lambdaQueryWrapper;
    }

    @Override
    public AgileSysTenant selectModel(Serializable id) {
        AgileSysTenant agileSysTenant = this.getById(id);
        if (agileSysTenant != null && agileSysTenant.isNotEmptyPk()) {
            String signData = agileSysTenant.getTenantCode() + agileSysTenant.getId();
            agileSysTenant.setTenantSign(DigestUtils.md2Hex(signData.getBytes()));
        }
        return agileSysTenant;
    }

    @Override
    public void saveModelValidate(AgileSysTenant agileSysTenant) {
        agileSysTenant.setAuditStatus(AgileAuditStatus.AUDIT.getCode());
        this.validateData(agileSysTenant);
    }

    @Override
    public boolean updateModel(AgileSysTenant agileSysTenant) {
        agileSysTenant.setTenantCode(null);

        this.updateModelValidate(agileSysTenant);
        AgileSysTenant agileSysTenantOld = this.getById(agileSysTenant.getId());
        if (AgileAuditStatus.PASS.getCode().equals(agileSysTenantOld.getAuditStatus())) {
            if (!agileSysTenant.getTenantName().equals(agileSysTenantOld.getTenantName())) {
                this.updateTenantAdminUser(agileSysTenant);
            }
        }
        if (this.updateById(agileSysTenant)) {
            return true;
        }
        return false;
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
    public boolean deleteModel(Serializable id) {
        AgileSysTenant agileSysTenant = this.getById(id);
        if (agileSysTenant == null || agileSysTenant.isEmptyPk()) {
            throw new AgileFrameException("租户信息已不存在！");
        }
        if (AgileAuditStatus.PASS.getCode().equals(agileSysTenant.getAuditStatus())) {
            this.deleteTenantAdminUser(agileSysTenant);
            this.deleteTenantInfo(agileSysTenant);
        }
        this.removeById(id);
        return true;
    }

    private void deleteTenantAdminUser(AgileSysTenant agileSysTenant) {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUser::getUserName, agileSysTenant.getTenantCode());
        agileSysUserService.remove(lambdaQueryWrapper);
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
        this.createTenantAdminUser(agileSysTenant);
    }


    private void updateTenantAdminUser(AgileSysTenant agileSysTenant) {
        LambdaUpdateWrapper<AgileSysUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileSysUser::getNickName, agileSysTenant.getTenantName());
        lambdaUpdateWrapper.eq(AgileSysUser::getUserName, agileSysTenant.getTenantCode());
        agileSysUserService.update(lambdaUpdateWrapper);
    }

    /**
     * 创建租户默认管理员
     *
     * @param agileSysTenant
     */
    private void createTenantAdminUser(AgileSysTenant agileSysTenant) {
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setUserName(agileSysTenant.getTenantCode());
        agileSysUser.setNickName(agileSysTenant.getTenantName());
        agileSysUser.setUserStatus(AgileUserStatus.NORMAL.getCode());
        agileSysUser.setUserSort(0);
        agileSysUser.setUserPwd(AgileSecurityUtil.encryptPassword(agileSysTenant.getTenantCode()));
        agileSysUserService.save(agileSysUser);
    }
}
