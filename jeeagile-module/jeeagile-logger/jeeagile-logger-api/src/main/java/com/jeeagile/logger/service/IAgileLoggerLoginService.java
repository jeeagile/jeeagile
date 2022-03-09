package com.jeeagile.logger.service;

import com.jeeagile.frame.service.IAgileService;
import com.jeeagile.logger.entity.AgileLoggerLogin;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileLoggerLoginService extends IAgileService<AgileLoggerLogin> {
    /**
     * 清空日志记录
     */
    boolean clearRecord();
}
