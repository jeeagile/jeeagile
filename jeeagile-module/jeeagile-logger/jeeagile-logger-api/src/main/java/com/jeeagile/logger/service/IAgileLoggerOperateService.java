package com.jeeagile.logger.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.logger.entity.AgileLoggerOperate;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileLoggerOperateService extends IAgileBaseService<AgileLoggerOperate> {

    /**
     * 分页查询操作日志数据
     */
    AgilePage<AgileLoggerOperate> selectPage(AgilePageable<AgileLoggerOperate> agilePageable);

    /**
     * 保存系统操作日志
     */
    boolean saveLoggerOperate(AgileLoggerOperate agileLoggerOperate);

    /**
     * 根据日志删除日志记录
     */
    boolean deleteLoggerOperate(String logId);

    /**
     * 清空日志记录
     */
    boolean clearLoggerOperate();
}
