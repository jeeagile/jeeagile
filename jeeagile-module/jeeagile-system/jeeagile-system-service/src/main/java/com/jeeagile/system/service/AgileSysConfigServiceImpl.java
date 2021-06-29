package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysConfig;
import com.jeeagile.system.mapper.AgileSysConfigMapper;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysConfigServiceImpl extends AgileBaseServiceImpl<AgileSysConfigMapper, AgileSysConfig> implements IAgileSysConfigService {

    @Override
    public AgilePage<AgileSysConfig> selectConfigPage(AgilePageable<AgileSysConfig> agilePageable) {
        return this.page(agilePageable, getSysConfigQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public List<AgileSysConfig> selectConfigList(AgileSysConfig agileSysConfig) {
        return this.list(getSysConfigQueryWrapper(agileSysConfig));
    }

    @Override
    public AgileSysConfig selectConfigById(String configId) {
        return this.getById(configId);
    }

    @Override
    public AgileSysConfig selectConfigByConfigKey(String configKey) {
        QueryWrapper<AgileSysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysConfig::getConfigKey, configKey);
        return this.getOne(queryWrapper);
    }

    @Override
    public AgileSysConfig saveConfig(AgileSysConfig agileSysConfig) {
        //校验业务数据
        agileSysConfig.validate();
        validateSysConfig(agileSysConfig);
        this.save(agileSysConfig);
        return agileSysConfig;
    }

    @Override
    public boolean updateConfigById(AgileSysConfig agileSysConfig) {
        //校验业务数据
        agileSysConfig.validate();
        validateSysConfig(agileSysConfig);
        return this.updateById(agileSysConfig);
    }

    @Override
    public boolean deleteConfigById(String configId) {
        AgileSysConfig agileSysConfig = this.getById(configId);
        if (agileSysConfig.getSystemFlag().equals(AgileFlagEnum.YES.getCode())) {
            throw new AgileValidateException("系统内置类型为‘是’的，不能删除！");
        }
        return this.removeById(configId);
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
        if (StringUtil.isEmpty(defaultPwd)) {
            defaultPwd = "888888";
        }
        return defaultPwd;
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysConfig> getSysConfigQueryWrapper(AgileSysConfig agileSysConfig) {
        QueryWrapper<AgileSysConfig> queryWrapper = new QueryWrapper<>();
        if (agileSysConfig != null) {
            if (StringUtil.isNotEmpty(agileSysConfig.getConfigName())) {
                queryWrapper.lambda().like(AgileSysConfig::getConfigName, agileSysConfig.getConfigName());
            }
            if (StringUtil.isNotEmpty(agileSysConfig.getConfigKey())) {
                queryWrapper.lambda().like(AgileSysConfig::getConfigKey, agileSysConfig.getConfigKey());
            }
            if (StringUtil.isNotEmpty(agileSysConfig.getSystemFlag())) {
                queryWrapper.lambda().like(AgileSysConfig::getSystemFlag, agileSysConfig.getSystemFlag());
            }
        }
        return queryWrapper;
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
            throw new AgileValidateException("参数名称或参数键名不能与已存在的数据重复！");
        }
    }
}
