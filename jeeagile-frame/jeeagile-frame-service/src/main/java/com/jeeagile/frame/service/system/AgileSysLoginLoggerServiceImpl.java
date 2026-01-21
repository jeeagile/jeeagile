package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.constants.AgileSuccessFail;
import com.jeeagile.frame.entity.system.AgileSysLoginLogger;
import com.jeeagile.frame.mapper.system.AgileSysLoginLoggerMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysLoginLoggerServiceImpl extends AgileBaseServiceImpl<AgileSysLoginLoggerMapper, AgileSysLoginLogger> implements IAgileSysLoginLoggerService {

    @Override
    public LambdaQueryWrapper<AgileSysLoginLogger> queryWrapper(AgileSysLoginLogger agileSysLoginLogger) {
        LambdaQueryWrapper<AgileSysLoginLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysLoginLogger != null) {
            if (AgileStringUtil.isNotEmpty(agileSysLoginLogger.getLoginName())) {
                lambdaQueryWrapper.eq(AgileSysLoginLogger::getLoginName, agileSysLoginLogger.getLoginName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysLoginLogger.getStatus())) {
                lambdaQueryWrapper.eq(AgileSysLoginLogger::getStatus, agileSysLoginLogger.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileSysLoginLogger::getLoginTime);
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileSysLoginLogger> selectExportData(AgileSysLoginLogger agileSysLoginLogger) {
        List<AgileSysLoginLogger> agileSysLoginLoggerList = this.selectList(agileSysLoginLogger);
        agileSysLoginLoggerList.forEach(item -> {
            item.setStatus(AgileSuccessFail.getDesc(item.getStatus()));
        });
        return agileSysLoginLoggerList;
    }

    @Override
    public boolean clearRecord() {
        return this.remove(new QueryWrapper<>());
    }

}
