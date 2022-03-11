package com.jeeagile.logger.aspect;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.util.AgileNetUtil;
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
class AgileLoggerAsyncTask {
    @AgileReference
    private IAgileLoggerOperateService agileLoggerOperateService;

    @AgileReference
    private IAgileLoggerLoginService agileLoggerLoginService;

    @Async("AgileLoggerAsyncTask")
    public void saveAgileLoggerOperate(AgileLoggerOperate agileLoggerOperate) {
        try {
            agileLoggerOperate.setOperateAddress(AgileNetUtil.getAddressByIp(agileLoggerOperate.getOperateIp()));
            agileLoggerOperateService.saveModel(agileLoggerOperate);
        } catch (Exception ex) {
            log.warn("操作日志记录发生异常!", ex);
        }
    }

    @Async("AgileLoggerAsyncTask")
    public void saveAgileLoggerLogin(AgileLoggerLogin agileLoggerLogin) {
        try {
            agileLoggerLogin.setLoginAddress(AgileNetUtil.getAddressByIp(agileLoggerLogin.getLoginIp()));
            agileLoggerLoginService.saveModel(agileLoggerLogin);
        } catch (Exception ex) {
            log.warn("登录日志记录发生异常!", ex);
        }
    }
}
