package com.jeeagile.frame.service.system;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.entity.system.AgileSysConfig;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysConfigService extends IAgileBaseService<AgileSysConfig> {
    /**
     * 获取系统参数配置信息
     */
    AgileSysConfig getSysConfig(String configKey);

    /**
     * 根据配置键值获取配置值
     */
    String getSysConfigValue(String configKey);

    /**
     * 获取默认配置密码
     */
    String getDefaultPassword();
}
