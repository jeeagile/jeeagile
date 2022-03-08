package com.jeeagile.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileServiceImpl;
import com.jeeagile.quartz.entity.AgileQuartzJobLogger;
import com.jeeagile.quartz.mapper.AgileQuartzJobLoggerMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileQuartzJobLoggerServiceImpl extends AgileServiceImpl<AgileQuartzJobLoggerMapper, AgileQuartzJobLogger> implements IAgileQuartzJobLoggerService {

    @Override
    public LambdaQueryWrapper<AgileQuartzJobLogger> queryWrapper(AgileQuartzJobLogger agileQuartzJobLogger) {
        LambdaQueryWrapper<AgileQuartzJobLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileQuartzJobLogger != null) {
            if (AgileStringUtil.isNotEmpty(agileQuartzJobLogger.getJobCode())) {
                lambdaQueryWrapper.eq(AgileQuartzJobLogger::getJobCode, agileQuartzJobLogger.getJobCode());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzJobLogger.getJobName())) {
                lambdaQueryWrapper.like(AgileQuartzJobLogger::getJobName, agileQuartzJobLogger.getJobName());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzJobLogger.getJobGroup())) {
                lambdaQueryWrapper.like(AgileQuartzJobLogger::getJobGroup, agileQuartzJobLogger.getJobGroup());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzJobLogger.getStatus())) {
                lambdaQueryWrapper.eq(AgileQuartzJobLogger::getStatus, agileQuartzJobLogger.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileQuartzJobLogger::getStartTime);
        return lambdaQueryWrapper;
    }

//    @Override
//    public AgilePage<AgileQuartzJobLogger> selectQuartzJobLoggerPage(AgilePageable<AgileQuartzJobLogger> agilePageable) {
//        return this.page(agilePageable, getQuartzJobLoggerQueryWrapper(agilePageable.getQueryCond()));
//    }

//    @Override
//    public AgileQuartzJobLogger selectQuartzJobLoggerById(String quartzLogId) {
//        return this.getById(quartzLogId);
//    }

//    @Override
//    public boolean deleteQuartzJobLogger(String quartzLogId) {
//        return this.removeById(quartzLogId);
//    }

    @Override
    public boolean clearQuartzJobLogger() {
        return this.remove(new QueryWrapper<>());
    }


}
