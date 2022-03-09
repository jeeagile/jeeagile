package com.jeeagile.logger.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileServiceImpl;
import com.jeeagile.logger.entity.AgileLoggerLogin;
import com.jeeagile.logger.mapper.AgileLoggerLoginMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileLoggerLoginServiceImpl extends AgileServiceImpl<AgileLoggerLoginMapper, AgileLoggerLogin> implements IAgileLoggerLoginService {

    @Override
    public LambdaQueryWrapper<AgileLoggerLogin> queryWrapper(AgileLoggerLogin agileLoggerLogin) {
        LambdaQueryWrapper<AgileLoggerLogin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileLoggerLogin != null) {
            if (AgileStringUtil.isNotEmpty(agileLoggerLogin.getLoginName())) {
                lambdaQueryWrapper.eq(AgileLoggerLogin::getLoginName, agileLoggerLogin.getLoginName());
            }
            if (AgileStringUtil.isNotEmpty(agileLoggerLogin.getStatus())) {
                lambdaQueryWrapper.eq(AgileLoggerLogin::getStatus, agileLoggerLogin.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileLoggerLogin::getLoginTime);
        return lambdaQueryWrapper;
    }

    @Override
    public boolean clearRecord() {
        return this.remove(new QueryWrapper<>());
    }

}
