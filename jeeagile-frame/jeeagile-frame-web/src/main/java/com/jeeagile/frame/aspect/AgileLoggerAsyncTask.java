package com.jeeagile.frame.aspect;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.util.AgileNetUtil;
import com.jeeagile.frame.entity.system.AgileSysOperateLogger;
import com.jeeagile.frame.entity.system.AgileSysLoginLogger;
import com.jeeagile.frame.service.system.IAgileSysOperateLoggerService;
import com.jeeagile.frame.service.system.IAgileSysLoginLoggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
@Component
public class AgileLoggerAsyncTask {
    @AgileReference
    private IAgileSysOperateLoggerService agileSysLoggerService;

    @AgileReference
    private IAgileSysLoginLoggerService agileSysLoginService;

    @Async("AgileLoggerTask")
    public void saveAgileSysLogger(AgileSysOperateLogger agileSysOperateLogger) {
        try {
            agileSysOperateLogger.setOperateAddress(AgileNetUtil.getAddressByIp(agileSysOperateLogger.getOperateIp()));
            agileSysLoggerService.saveModel(agileSysOperateLogger);
        } catch (Exception ex) {
            log.warn("操作日志记录发生异常!", ex);
        }
    }

    @Async("AgileLoggerTask")
    public void saveAgileSysLogin(AgileSysLoginLogger agileSysLoginLogger) {
        try {
            agileSysLoginLogger.setLoginAddress(AgileNetUtil.getAddressByIp(agileSysLoginLogger.getLoginIp()));
            agileSysLoginService.saveModel(agileSysLoginLogger);
        } catch (Exception ex) {
            log.warn("登录日志记录发生异常!", ex);
        }
    }
}
