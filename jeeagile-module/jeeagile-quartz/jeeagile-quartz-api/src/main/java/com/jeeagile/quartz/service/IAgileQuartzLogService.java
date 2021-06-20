package com.jeeagile.quartz.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.quartz.entity.AgileQuartzLog;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileQuartzLogService extends IAgileBaseService<AgileQuartzLog> {
    /**
     * 分页查询任务日志
     */
    AgilePage<AgileQuartzLog> selectQuartzLogPage(AgilePageable<AgileQuartzLog> agilePageable);

    /**
     * 查看任务日志详细信息
     */
    AgileQuartzLog selectQuartzLogById(String quartzLogId);

    /**
     * 删除任务执行日志
     */
    boolean deleteQuartzLog(String quartzLogId);

    /**
     * 清空任务执行日志
     */
    boolean clearQuartzLog();
}
