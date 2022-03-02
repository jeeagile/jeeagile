package com.jeeagile.quartz.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileService;
import com.jeeagile.quartz.entity.AgileQuartzJobLogger;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileQuartzJobLoggerService extends IAgileService<AgileQuartzJobLogger> {
    /**
     * 分页查询任务日志
     */
    AgilePage<AgileQuartzJobLogger> selectQuartzJobLoggerPage(AgilePageable<AgileQuartzJobLogger> agilePageable);

    /**
     * 查看任务日志详细信息
     */
    AgileQuartzJobLogger selectQuartzJobLoggerById(String quartzLogId);

    /**
     * 删除任务执行日志
     */
    boolean deleteQuartzJobLogger(String quartzLogId);

    /**
     * 清空任务执行日志
     */
    boolean clearQuartzJobLogger();
}
