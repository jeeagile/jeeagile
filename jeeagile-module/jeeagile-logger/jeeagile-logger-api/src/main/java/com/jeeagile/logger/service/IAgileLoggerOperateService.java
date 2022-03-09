package com.jeeagile.logger.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.logger.entity.AgileLoggerOperate;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileLoggerOperateService extends IAgileBaseService<AgileLoggerOperate> {
    /**
     * 清空日志记录
     */
    boolean clearRecord();
}
