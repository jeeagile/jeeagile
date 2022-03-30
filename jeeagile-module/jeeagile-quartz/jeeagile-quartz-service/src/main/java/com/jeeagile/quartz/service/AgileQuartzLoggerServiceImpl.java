package com.jeeagile.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.quartz.entity.AgileQuartzLogger;
import com.jeeagile.quartz.mapper.AgileQuartzLoggerMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileQuartzLoggerServiceImpl extends AgileBaseServiceImpl<AgileQuartzLoggerMapper, AgileQuartzLogger> implements IAgileQuartzLoggerService {

    @Override
    public LambdaQueryWrapper<AgileQuartzLogger> queryWrapper(AgileQuartzLogger agileQuartzLogger) {
        LambdaQueryWrapper<AgileQuartzLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileQuartzLogger != null) {
            if (AgileStringUtil.isNotEmpty(agileQuartzLogger.getJobCode())) {
                lambdaQueryWrapper.eq(AgileQuartzLogger::getJobCode, agileQuartzLogger.getJobCode());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzLogger.getJobName())) {
                lambdaQueryWrapper.like(AgileQuartzLogger::getJobName, agileQuartzLogger.getJobName());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzLogger.getJobGroup())) {
                lambdaQueryWrapper.like(AgileQuartzLogger::getJobGroup, agileQuartzLogger.getJobGroup());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzLogger.getStatus())) {
                lambdaQueryWrapper.eq(AgileQuartzLogger::getStatus, agileQuartzLogger.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileQuartzLogger::getStartTime);
        return lambdaQueryWrapper;
    }

    @Override
    public boolean clearRecord() {
        return this.remove(new QueryWrapper<>());
    }
}
