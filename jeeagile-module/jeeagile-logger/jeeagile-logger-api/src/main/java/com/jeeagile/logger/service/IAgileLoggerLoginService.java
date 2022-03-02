package com.jeeagile.logger.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileService;
import com.jeeagile.logger.entity.AgileLoggerLogin;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileLoggerLoginService extends IAgileService<AgileLoggerLogin> {
    /**
     * 分页查询参数配置数据
     */
    AgilePage<AgileLoggerLogin> selectLoggerPage(AgilePageable<AgileLoggerLogin> agilePageable);

    /**
     * 保存系统操作日志
     */
    boolean saveLoggerLogin(AgileLoggerLogin agileLoggerLogin);

    /**
     * 根据日志删除日志记录
     */
    boolean deleteLoggerLogin(String logId);

    /**
     * 清空日志记录
     */
    boolean clearLoggerLogin();
}
