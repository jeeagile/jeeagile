package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.constants.SysYesNo;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.entity.system.AgileSysConfig;
import com.jeeagile.frame.mapper.system.AgileSysConfigMapper;

import java.io.Serializable;
import java.util.List;

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
    public List<AgileSysConfig> selectExportData(AgileSysConfig agileSysConfig) {
        List<AgileSysConfig> agileSysConfigList = this.selectList(agileSysConfig);
        agileSysConfigList.forEach(item -> item.setSystemFlag(SysYesNo.getDesc(item.getSystemFlag())));
        return agileSysConfigList;
    }

    @Override
    public AgileSysConfig getSysConfig(String configKey) {
        LambdaQueryWrapper<AgileSysConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysConfig::getConfigKey, configKey);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public void saveModelValidate(AgileSysConfig agileSysConfig) {
        this.validateData(agileSysConfig);
    }

    @Override
    public void updateModelValidate(AgileSysConfig agileSysConfig) {
        this.validateData(agileSysConfig);
    }

    @Override
    public void deleteModelValidate(Serializable id) {
        AgileSysConfig agileSysConfig = this.getById(id);
        if (SysYesNo.YES.equals(agileSysConfig.getSystemFlag())) {
            throw new AgileValidateException("系统内置类型为‘是’的，不能删除！");
        }
    }


    @Override
    public String getSysConfigValue(String configKey) {
        AgileSysConfig agileSysConfig = this.getSysConfig(configKey);
        if (agileSysConfig != null) {
            return agileSysConfig.getConfigValue();
        }
        return null;
    }

    @Override
    public String getDefaultPassword() {
        String defaultPassword = getSysConfigValue("sys.user.pwd");
        if (AgileStringUtil.isEmpty(defaultPassword)) {
            defaultPassword = "888888";
        }
        return defaultPassword;
    }

    /**
     * 校验参数名称或参数键名不能与已存在的数据重复
     */
    private void validateData(AgileSysConfig agileSysConfig) {
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
