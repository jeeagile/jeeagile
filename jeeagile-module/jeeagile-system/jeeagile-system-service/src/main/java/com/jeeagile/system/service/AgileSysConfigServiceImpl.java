package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysConfig;
import com.jeeagile.system.mapper.AgileSysConfigMapper;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysConfigServiceImpl extends AgileBaseServiceImpl<AgileSysConfigMapper, AgileSysConfig> implements IAgileSysConfigService {

    @Override
    public LambdaQueryWrapper<AgileSysConfig> queryWrapper(AgileSysConfig agileSysConfig) {
        LambdaQueryWrapper<AgileSysConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysConfig != null) {
            if (AgileStringUtil.isNotEmpty(agileSysConfig.getConfigName())) {
                lambdaQueryWrapper.like(AgileSysConfig::getConfigName, agileSysConfig.getConfigName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysConfig.getConfigKey())) {
                lambdaQueryWrapper.eq(AgileSysConfig::getConfigKey, agileSysConfig.getConfigKey());
            }
            if (AgileStringUtil.isNotEmpty(agileSysConfig.getSystemFlag())) {
                lambdaQueryWrapper.eq(AgileSysConfig::getSystemFlag, agileSysConfig.getSystemFlag());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public AgileSysConfig selectConfigByConfigKey(String configKey) {
        QueryWrapper<AgileSysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysConfig::getConfigKey, configKey);
        return this.getOne(queryWrapper);
    }

    @Override
    public void saveModelValidate(AgileSysConfig agileSysConfig) {
        this.validateSysConfig(agileSysConfig);
    }

    @Override
    public void updateModelValidate(AgileSysConfig agileSysConfig) {
        this.validateSysConfig(agileSysConfig);
    }

    @Override
    public void deleteModelValidate(Serializable id) {
        AgileSysConfig agileSysConfig = this.getById(id);
        if (agileSysConfig.getSystemFlag().equals(AgileFlagEnum.YES.getCode())) {
            throw new AgileValidateException("系统内置类型为‘是’的，不能删除！");
        }
    }


    @Override
    public String getConfigValueByKey(String configKey) {
        AgileSysConfig agileSysConfig = this.selectConfigByConfigKey(configKey);
        if (agileSysConfig != null) {
            return agileSysConfig.getConfigValue();
        }
        return null;
    }

    @Override
    public String getDefaultPwd() {
        String defaultPwd = getConfigValueByKey("sys.user.pwd");
        if (AgileStringUtil.isEmpty(defaultPwd)) {
            defaultPwd = "888888";
        }
        return defaultPwd;
    }

    /**
     * 校验参数名称或参数键名不能与已存在的数据重复
     */
    private void validateSysConfig(AgileSysConfig agileSysConfig) {
        LambdaQueryWrapper<AgileSysConfig> queryWrapper = new LambdaQueryWrapper<>();
        if (agileSysConfig.getId() != null) {
            queryWrapper.ne(AgileSysConfig::getId, agileSysConfig.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileSysConfig::getConfigKey, agileSysConfig.getConfigKey()).or().eq(AgileSysConfig::getConfigName, agileSysConfig.getConfigName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("参数名称或参数键名已存在！");
        }
    }
}
