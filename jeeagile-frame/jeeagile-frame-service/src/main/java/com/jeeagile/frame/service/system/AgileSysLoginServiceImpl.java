package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysLogin;
import com.jeeagile.frame.mapper.system.AgileSysLoginMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysLoginServiceImpl extends AgileBaseServiceImpl<AgileSysLoginMapper, AgileSysLogin> implements IAgileSysLoginService {

    @Override
    public LambdaQueryWrapper<AgileSysLogin> queryWrapper(AgileSysLogin agileSysLogin) {
        LambdaQueryWrapper<AgileSysLogin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysLogin != null) {
            if (AgileStringUtil.isNotEmpty(agileSysLogin.getLoginName())) {
                lambdaQueryWrapper.eq(AgileSysLogin::getLoginName, agileSysLogin.getLoginName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysLogin.getStatus())) {
                lambdaQueryWrapper.eq(AgileSysLogin::getStatus, agileSysLogin.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileSysLogin::getLoginTime);
        return lambdaQueryWrapper;
    }

    @Override
    public boolean clearRecord() {
        return this.remove(new QueryWrapper<>());
    }

}
