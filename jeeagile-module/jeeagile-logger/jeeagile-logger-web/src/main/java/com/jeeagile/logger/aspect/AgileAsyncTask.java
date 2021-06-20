package com.jeeagile.logger.aspect;

import com.jeeagile.core.protocol.annotation.AgileProvider;
import com.jeeagile.core.util.AddressUtil;
import com.jeeagile.logger.entity.AgileLoggerLogin;
import com.jeeagile.logger.entity.AgileLoggerOperate;
import com.jeeagile.logger.service.IAgileLoggerLoginService;
import com.jeeagile.logger.service.IAgileLoggerOperateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Component
@Slf4j
class AgileAsyncTask {
    @AgileProvider
    private IAgileLoggerOperateService agileLoggerOperateService;

    @AgileProvider
    private IAgileLoggerLoginService agileLoggerLoginService;

    @Async("AgileAsyncTask")
    public void saveAgileLoggerOperate(AgileLoggerOperate agileLoggerOperate) {
        try {
            agileLoggerOperate.setRemoteLocation(AddressUtil.getRealAddressByIp(agileLoggerOperate.getRemoteIp()));
            agileLoggerOperateService.saveLoggerOperate(agileLoggerOperate);
        } catch (Exception ex) {
            log.warn("操作日志记录发生异常!", ex);
        }
    }

    @Async("AgileAsyncTask")
    public void saveAgileLoggerLogin(AgileLoggerLogin agileLoggerLogin) {
        try {
            agileLoggerLogin.setRemoteLocation(AddressUtil.getRealAddressByIp(agileLoggerLogin.getRemoteIp()));
            agileLoggerLoginService.saveLoggerLogin(agileLoggerLogin);
        } catch (Exception ex) {
            log.warn("登录日志记录发生异常!", ex);
        }
    }
}
