package com.jeeagile.drools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.entity.AgileDroolsRuleLogger;
import com.jeeagile.drools.mapper.AgileDroolsRuleLoggerMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:35
 * @description 规则引擎 场景规则执行日志 业务处理层
 */
@AgileService
public class AgileDroolsRuleLoggerServiceImpl extends AgileBaseServiceImpl<AgileDroolsRuleLoggerMapper, AgileDroolsRuleLogger> implements IAgileDroolsRuleLoggerService {
    /**
     * 拼装查询条件
     */
    @Override
    public LambdaQueryWrapper<AgileDroolsRuleLogger> queryWrapper(AgileDroolsRuleLogger agileDroolsRuleLogger) {
        LambdaQueryWrapper<AgileDroolsRuleLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsRuleLogger != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsRuleLogger.getLoggerId())) {
                lambdaQueryWrapper.eq(AgileDroolsRuleLogger::getLoggerId, agileDroolsRuleLogger.getLoggerId());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRuleLogger.getRuleName())) {
                lambdaQueryWrapper.like(AgileDroolsRuleLogger::getRuleName, agileDroolsRuleLogger.getRuleName());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRuleLogger.getStartTime())) {
                lambdaQueryWrapper.eq(AgileDroolsRuleLogger::getStartTime, agileDroolsRuleLogger.getStartTime());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRuleLogger.getEndTime())) {
                lambdaQueryWrapper.eq(AgileDroolsRuleLogger::getEndTime, agileDroolsRuleLogger.getEndTime());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRuleLogger.getExecuteTime())) {
                lambdaQueryWrapper.eq(AgileDroolsRuleLogger::getExecuteTime, agileDroolsRuleLogger.getExecuteTime());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileDroolsRuleLogger> droolsRuleLoggerList(String loggerId) {
        LambdaQueryWrapper<AgileDroolsRuleLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsRuleLogger::getLoggerId, loggerId);
        return this.list(lambdaQueryWrapper);
    }
}
