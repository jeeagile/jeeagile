package com.jeeagile.logger.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
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
    public AgilePage<AgileLoggerLogin> selectLoggerPage(AgilePageable<AgileLoggerLogin> agilePageable) {
        return this.page(agilePageable, getLoggerLoginQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public boolean saveLoggerLogin(AgileLoggerLogin agileLoggerLogin) {
        return this.save(agileLoggerLogin);
    }

    @Override
    public boolean deleteLoggerLogin(String logId) {
        return this.removeById(logId);
    }

    @Override
    public boolean clearLoggerLogin() {
        return this.remove(new QueryWrapper<>());
    }

    private QueryWrapper<AgileLoggerLogin> getLoggerLoginQueryWrapper(AgileLoggerLogin agileLoggerLogin) {
        QueryWrapper<AgileLoggerLogin> queryWrapper = new QueryWrapper<>();
        if (agileLoggerLogin != null) {
            if (StringUtil.isNotEmpty(agileLoggerLogin.getLoginName())) {
                queryWrapper.lambda().eq(AgileLoggerLogin::getLoginName, agileLoggerLogin.getLoginName());
            }
            if (StringUtil.isNotEmpty(agileLoggerLogin.getStatus())) {
                queryWrapper.lambda().eq(AgileLoggerLogin::getStatus, agileLoggerLogin.getStatus());
            }
        }
        queryWrapper.lambda().orderByDesc(AgileLoggerLogin::getLoginTime);
        return queryWrapper;
    }

}
