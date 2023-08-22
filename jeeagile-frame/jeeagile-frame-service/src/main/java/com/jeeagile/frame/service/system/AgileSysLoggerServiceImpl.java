package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.constants.SysOperateType;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.constants.SysSuccessFail;
import com.jeeagile.frame.entity.system.AgileSysLogger;
import com.jeeagile.frame.mapper.system.AgileSysLoggerMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysLoggerServiceImpl extends AgileBaseServiceImpl<AgileSysLoggerMapper, AgileSysLogger> implements IAgileSysLoggerService {

    @Override
    public LambdaQueryWrapper<AgileSysLogger> queryWrapper(AgileSysLogger agileSysLogger) {
        LambdaQueryWrapper<AgileSysLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysLogger != null) {
            if (AgileStringUtil.isNotEmpty(agileSysLogger.getOperateModule())) {
                lambdaQueryWrapper.like(AgileSysLogger::getOperateModule, agileSysLogger.getOperateModule());
            }
            if (AgileStringUtil.isNotEmpty(agileSysLogger.getOperateType())) {
                lambdaQueryWrapper.eq(AgileSysLogger::getOperateType, agileSysLogger.getOperateType());
            }
            if (AgileStringUtil.isNotEmpty(agileSysLogger.getStatus())) {
                lambdaQueryWrapper.eq(AgileSysLogger::getStatus, agileSysLogger.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileSysLogger::getCreateTime);
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileSysLogger> selectExportData(AgileSysLogger agileSysLogger) {
        List<AgileSysLogger> agileSysLoggerList = this.selectList(agileSysLogger);
        agileSysLoggerList.forEach(item -> {
            item.setStatus(SysSuccessFail.getDesc(item.getStatus()));
            item.setOperateType(SysOperateType.getDesc(item.getOperateType()));
        });
        return agileSysLoggerList;
    }

    @Override
    public boolean clearRecord() {
        return this.remove(new QueryWrapper<>());
    }
}
