package com.jeeagile.drools.service;

import com.jeeagile.drools.entity.AgileDroolsRuleLogger;
import com.jeeagile.frame.service.IAgileBaseService;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:35
 * @description 规则引擎 场景规则执行日志 Service接口
 */
public interface IAgileDroolsRuleLoggerService extends IAgileBaseService<AgileDroolsRuleLogger> {
    /**
     * 日志
     */
    List<AgileDroolsRuleLogger> droolsRuleLoggerList(String loggerId);
}
