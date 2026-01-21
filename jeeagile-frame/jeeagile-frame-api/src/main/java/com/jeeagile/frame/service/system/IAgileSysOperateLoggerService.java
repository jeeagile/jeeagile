package com.jeeagile.frame.service.system;

import com.jeeagile.frame.entity.system.AgileSysOperateLogger;
import com.jeeagile.frame.service.IAgileBaseService;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysOperateLoggerService extends IAgileBaseService<AgileSysOperateLogger> {
    /**
     * 清空日志记录
     */
    boolean clearRecord();
}
