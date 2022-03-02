package com.jeeagile.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
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
    public AgilePage<AgileQuartzJobLogger> selectQuartzJobLoggerPage(AgilePageable<AgileQuartzJobLogger> agilePageable) {
        return this.page(agilePageable, getQuartzJobLoggerQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public AgileQuartzJobLogger selectQuartzJobLoggerById(String quartzLogId) {
        return this.getById(quartzLogId);
    }

    @Override
    public boolean deleteQuartzJobLogger(String quartzLogId) {
        return this.removeById(quartzLogId);
    }

    @Override
    public boolean clearQuartzJobLogger() {
        return this.remove(new QueryWrapper<>());
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileQuartzJobLogger> getQuartzJobLoggerQueryWrapper(AgileQuartzJobLogger agileQuartzJobLogger) {
        QueryWrapper<AgileQuartzJobLogger> queryWrapper = new QueryWrapper<>();
        if (agileQuartzJobLogger != null) {
            if (StringUtil.isNotEmpty(agileQuartzJobLogger.getJobCode())) {
                queryWrapper.lambda().eq(AgileQuartzJobLogger::getJobCode, agileQuartzJobLogger.getJobCode());
            }
            if (StringUtil.isNotEmpty(agileQuartzJobLogger.getJobName())) {
                queryWrapper.lambda().like(AgileQuartzJobLogger::getJobName, agileQuartzJobLogger.getJobName());
            }
            if (StringUtil.isNotEmpty(agileQuartzJobLogger.getJobGroup())) {
                queryWrapper.lambda().like(AgileQuartzJobLogger::getJobGroup, agileQuartzJobLogger.getJobGroup());
            }
            if (StringUtil.isNotEmpty(agileQuartzJobLogger.getStatus())) {
                queryWrapper.lambda().eq(AgileQuartzJobLogger::getStatus, agileQuartzJobLogger.getStatus());
            }
        }
        queryWrapper.lambda().orderByDesc(AgileQuartzJobLogger::getStartTime);
        return queryWrapper;
    }

}
