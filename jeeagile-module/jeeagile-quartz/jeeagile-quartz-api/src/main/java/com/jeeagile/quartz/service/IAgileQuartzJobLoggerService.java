package com.jeeagile.quartz.service;

import com.jeeagile.frame.service.IAgileService;
import com.jeeagile.quartz.entity.AgileQuartzJobLogger;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileQuartzJobLoggerService extends IAgileService<AgileQuartzJobLogger> {
    /**
     * 清空任务执行日志
     */
    boolean clearRecord();
}
