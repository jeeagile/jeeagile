package com.jeeagile.frame.aspect;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.util.AgileNetUtil;
import com.jeeagile.frame.entity.system.AgileSysLogger;
import com.jeeagile.frame.entity.system.AgileSysLogin;
import com.jeeagile.frame.service.system.IAgileSysLoggerService;
import com.jeeagile.frame.service.system.IAgileSysLoginService;
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
    private IAgileSysLoggerService agileSysLoggerService;

    @AgileReference
    private IAgileSysLoginService agileSysLoginService;

    @Async("AgileAsyncTask")
    public void saveAgileSysLogger(AgileSysLogger agileSysLogger) {
        try {
            agileSysLogger.setOperateAddress(AgileNetUtil.getAddressByIp(agileSysLogger.getOperateIp()));
            agileSysLoggerService.saveModel(agileSysLogger);
        } catch (Exception ex) {
            log.warn("操作日志记录发生异常!", ex);
        }
    }

    @Async("AgileAsyncTask")
    public void saveAgileSysLogin(AgileSysLogin agileSysLogin) {
        try {
            agileSysLogin.setLoginAddress(AgileNetUtil.getAddressByIp(agileSysLogin.getLoginIp()));
            agileSysLoginService.saveModel(agileSysLogin);
        } catch (Exception ex) {
            log.warn("登录日志记录发生异常!", ex);
        }
    }
}
