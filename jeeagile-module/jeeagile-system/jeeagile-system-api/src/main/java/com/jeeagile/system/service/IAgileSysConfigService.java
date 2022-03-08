package com.jeeagile.system.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysConfig;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysConfigService extends IAgileBaseService<AgileSysConfig> {
//    /**
//     * 分页查询参数配置数据
//     */
//    AgilePage<AgileSysConfig> selectConfigPage(AgilePageable<AgileSysConfig> agilePageable);
//
//    /**
//     * 查询参数配置数据
//     */
//    List<AgileSysConfig> selectConfigList(AgileSysConfig agileSysConfig);
//
//    /**
//     * 查看参数配置信息
//     */
//    AgileSysConfig selectConfigById(String configId);


    /**
     * 根据配置键值获取配置信息
     */
    AgileSysConfig selectConfigByConfigKey(String configKey);
//
//    /**
//     * 新增参数配置
//     */
//    AgileSysConfig saveConfig(AgileSysConfig agileSysConfig);
//
//    /**
//     * 更新参数配置
//     */
//    boolean updateConfigById(AgileSysConfig agileSysConfig);
//
//    /**
//     * 删除参数配置
//     */
//    boolean deleteConfigById(String configId);

    /**
     * 根据配置键值获取配置值
     */
    String getConfigValueByKey(String configKey);

    /**
     * 获取默认配置密码
     */
    String getDefaultPwd();
}
