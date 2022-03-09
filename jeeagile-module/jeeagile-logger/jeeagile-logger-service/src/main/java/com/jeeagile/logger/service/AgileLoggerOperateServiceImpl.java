package com.jeeagile.logger.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.logger.entity.AgileLoggerOperate;
import com.jeeagile.logger.mapper.AgileLoggerOperateMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileLoggerOperateServiceImpl extends AgileBaseServiceImpl<AgileLoggerOperateMapper, AgileLoggerOperate> implements IAgileLoggerOperateService {

    @Override
    public LambdaQueryWrapper<AgileLoggerOperate> queryWrapper(AgileLoggerOperate agileLoggerOperate) {
        LambdaQueryWrapper<AgileLoggerOperate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileLoggerOperate != null) {
            if (AgileStringUtil.isNotEmpty(agileLoggerOperate.getType())) {
                lambdaQueryWrapper.eq(AgileLoggerOperate::getType, agileLoggerOperate.getType());
            }
            if (AgileStringUtil.isNotEmpty(agileLoggerOperate.getTitle())) {
                lambdaQueryWrapper.like(AgileLoggerOperate::getTitle, agileLoggerOperate.getTitle());
            }
            if (AgileStringUtil.isNotEmpty(agileLoggerOperate.getStatus())) {
                lambdaQueryWrapper.eq(AgileLoggerOperate::getStatus, agileLoggerOperate.getStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileLoggerOperate::getCreateTime);
        return lambdaQueryWrapper;
    }

    @Override
    public boolean clearRecord() {
        return this.remove(new QueryWrapper<>());
    }
}
