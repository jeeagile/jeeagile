package com.jeeagile.drools.service;

import com.jeeagile.drools.entity.AgileDroolsSceneLogger;
import com.jeeagile.drools.vo.AgileDroolsLoggerInfo;
import com.jeeagile.drools.vo.AgileDroolsSceneInfo;
import com.jeeagile.frame.service.IAgileBaseService;

import java.io.Serializable;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 场景执行日志 Service接口
 */
public interface IAgileDroolsSceneLoggerService extends IAgileBaseService<AgileDroolsSceneLogger> {
    /**
     * 获取场景执行信息
     */
    AgileDroolsLoggerInfo loggerInfo(Serializable loggerId);

    /**
     * 统计信息
     */
    Map statisticInfo(AgileDroolsSceneLogger agileDroolsSceneLogger);
}
