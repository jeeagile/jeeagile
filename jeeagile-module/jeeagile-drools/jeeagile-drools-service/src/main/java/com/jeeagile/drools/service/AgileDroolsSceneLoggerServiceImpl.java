package com.jeeagile.drools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.entity.AgileDroolsSceneLogger;
import com.jeeagile.drools.mapper.AgileDroolsSceneLoggerMapper;
import com.jeeagile.drools.vo.AgileDroolsLoggerInfo;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 场景执行日志 业务处理层
 */
@AgileService
public class AgileDroolsSceneLoggerServiceImpl extends AgileBaseServiceImpl<AgileDroolsSceneLoggerMapper, AgileDroolsSceneLogger> implements IAgileDroolsSceneLoggerService {
    @Autowired
    private IAgileDroolsRuleLoggerService agileDroolsRuleLoggerService;

    /**
     * 拼装查询条件
     */
    @Override
    public LambdaQueryWrapper<AgileDroolsSceneLogger> queryWrapper(AgileDroolsSceneLogger agileDroolsSceneLogger) {
        LambdaQueryWrapper<AgileDroolsSceneLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsSceneLogger != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneLogger.getSceneId())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneLogger::getSceneId, agileDroolsSceneLogger.getSceneId());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneLogger.getSceneCode())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneLogger::getSceneCode, agileDroolsSceneLogger.getSceneCode());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneLogger.getSceneName())) {
                lambdaQueryWrapper.like(AgileDroolsSceneLogger::getSceneName, agileDroolsSceneLogger.getSceneName());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneLogger.getRuleCount())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneLogger::getRuleCount, agileDroolsSceneLogger.getRuleCount());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneLogger.getExecuteParam())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneLogger::getExecuteParam, agileDroolsSceneLogger.getExecuteParam());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneLogger.getExecuteResult())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneLogger::getExecuteResult, agileDroolsSceneLogger.getExecuteResult());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneLogger.getExecuteStatus())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneLogger::getExecuteStatus, agileDroolsSceneLogger.getExecuteStatus());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public AgileDroolsLoggerInfo loggerInfo(Serializable loggerId) {
        AgileDroolsLoggerInfo droolsLoggerInfo = new AgileDroolsLoggerInfo();
        AgileDroolsSceneLogger agileDroolsSceneLogger = this.getById(loggerId);
        BeanUtils.copyProperties(agileDroolsSceneLogger, droolsLoggerInfo);
        droolsLoggerInfo.setDroolsRuleLoggerList(agileDroolsRuleLoggerService.droolsRuleLoggerList((String) loggerId));
        return droolsLoggerInfo;
    }

    @Override
    public Map statisticInfo(AgileDroolsSceneLogger agileDroolsSceneLogger) {
        Map map = new HashMap();
        map.put("executeCount", this.baseMapper.getExecuteCount(agileDroolsSceneLogger.getSceneId()));
        map.put("successCount", this.baseMapper.getSuccessCount(agileDroolsSceneLogger.getSceneId()));
        map.put("errorCount", this.baseMapper.getErrorCount(agileDroolsSceneLogger.getSceneId()));
        map.put("maxTime", this.baseMapper.getMaxTime(agileDroolsSceneLogger.getSceneId()));
        map.put("averageTime", this.baseMapper.getAverageTime(agileDroolsSceneLogger.getSceneId()));
        map.put("minTime", this.baseMapper.getMinTime(agileDroolsSceneLogger.getSceneId()));

        if (AgileStringUtil.isEmpty(agileDroolsSceneLogger.getStartTime())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -10);
            agileDroolsSceneLogger.setStartTime(calendar.getTime());
        }
        if (AgileStringUtil.isEmpty(agileDroolsSceneLogger.getEndTime())) {
            agileDroolsSceneLogger.setEndTime(new Date());
        }
        map.put("statisticExecuteCount", this.baseMapper.statisticExecuteCount(agileDroolsSceneLogger));
        map.put("statisticSuccessCount", this.baseMapper.statisticSuccessCount(agileDroolsSceneLogger));
        map.put("statisticErrorCount", this.baseMapper.statisticErrorCount(agileDroolsSceneLogger));
        map.put("statisticMaxTime", this.baseMapper.statisticMaxTime(agileDroolsSceneLogger));
        map.put("statisticAverageTime", this.baseMapper.statisticAverageTime(agileDroolsSceneLogger));
        map.put("statisticMinTime", this.baseMapper.statisticMinTime(agileDroolsSceneLogger));
        return map;
    }
}
