package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysConfig;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysConfigService extends IAgileBaseService<AgileSysConfig> {
    /**
     * 根据配置键值获取配置信息
     */
    AgileSysConfig selectConfigByConfigKey(String configKey);

    /**
     * 根据配置键值获取配置值
     */
    String getConfigValueByKey(String configKey);

    /**
     * 获取默认配置密码
     */
    String getDefaultPassword();
}
