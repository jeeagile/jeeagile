package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.constants.AgileSuccessFail;
import com.jeeagile.frame.entity.system.AgileSysOperateLogger;
import com.jeeagile.frame.mapper.system.AgileSysOperateLoggerMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysOperateLoggerServiceImpl extends AgileBaseServiceImpl<AgileSysOperateLoggerMapper, AgileSysOperateLogger> implements IAgileSysOperateLoggerService {

    @Override
    public LambdaQueryWrapper<AgileSysOperateLogger> queryWrapper(AgileSysOperateLogger agileSysOperateLogger) {
        LambdaQueryWrapper<AgileSysOperateLogger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysOperateLogger != null) {
            if (AgileStringUtil.isNotEmpty(agileSysOperateLogger.getOperateModule())) {
                lambdaQueryWrapper.like(AgileSysOperateLogger::getOperateModule, agileSysOperateLogger.getOperateModule());
            }
            if (AgileStringUtil.isNotEmpty(agileSysOperateLogger.getOperateType())) {
                lambdaQueryWrapper.eq(AgileSysOperateLogger::getOperateType, agileSysOperateLogger.getOperateType());
            }
            if (AgileStringUtil.isNotEmpty(agileSysOperateLogger.getStatus())) {
                lambdaQueryWrapper.eq(AgileSysOperateLogger::getStatus, agileSysOperateLogger.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileSysOperateLogger::getCreateTime);
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileSysOperateLogger> selectExportData(AgileSysOperateLogger agileSysOperateLogger) {
        List<AgileSysOperateLogger> agileSysOperateLoggerList = this.selectList(agileSysOperateLogger);
        agileSysOperateLoggerList.forEach(item -> {
            item.setStatus(AgileSuccessFail.getDesc(item.getStatus()));
            item.setOperateType(AgileOperateType.getDesc(item.getOperateType()));
        });
        return agileSysOperateLoggerList;
    }

    @Override
    public boolean clearRecord() {
        return this.remove(new QueryWrapper<>());
    }
}
