package com.jeeagile.frame.service.system;

import com.jeeagile.frame.entity.system.AgileSysLogin;
import com.jeeagile.frame.service.IAgileBaseService;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysLoginService extends IAgileBaseService<AgileSysLogin> {
    /**
     * 清空日志记录
     */
    boolean clearRecord();
}
