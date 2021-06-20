package com.jeeagile.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.quartz.entity.AgileQuartzLog;
import com.jeeagile.quartz.mapper.AgileQuartzLogMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileQuartzLogServiceImpl extends AgileBaseServiceImpl<AgileQuartzLogMapper, AgileQuartzLog> implements IAgileQuartzLogService {

    @Override
    public AgilePage<AgileQuartzLog> selectQuartzLogPage(AgilePageable<AgileQuartzLog> agilePageable) {
        return this.page(agilePageable, getQuartzJobQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public AgileQuartzLog selectQuartzLogById(String quartzLogId) {
        return this.getById(quartzLogId);
    }

    @Override
    public boolean deleteQuartzLog(String quartzLogId) {
        return this.removeById(quartzLogId);
    }

    @Override
    public boolean clearQuartzLog() {
        return this.remove(new QueryWrapper<>());
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileQuartzLog> getQuartzJobQueryWrapper(AgileQuartzLog agileQuartzLog) {
        QueryWrapper<AgileQuartzLog> queryWrapper = new QueryWrapper<>();
        if (agileQuartzLog != null) {

        }
        return queryWrapper;
    }

}
