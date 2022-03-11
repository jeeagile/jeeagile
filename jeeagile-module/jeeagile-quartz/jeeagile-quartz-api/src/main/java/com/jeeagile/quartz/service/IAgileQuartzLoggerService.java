package com.jeeagile.quartz.service;

import com.jeeagile.frame.service.IAgileService;
import com.jeeagile.quartz.entity.AgileQuartzLogger;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileQuartzLoggerService extends IAgileService<AgileQuartzLogger> {
    /**
     * 清空任务执行日志
     */
    boolean clearRecord();
}
