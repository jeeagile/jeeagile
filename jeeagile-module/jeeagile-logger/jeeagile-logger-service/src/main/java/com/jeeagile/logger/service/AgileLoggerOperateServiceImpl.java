package com.jeeagile.logger.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
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
    public AgilePage<AgileLoggerOperate> selectLoggerPage(AgilePageable<AgileLoggerOperate> agilePageable) {
        return this.page(agilePageable, getLoggerOperateQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public boolean saveLoggerOperate(AgileLoggerOperate agileLoggerOperate) {
        return this.save(agileLoggerOperate);
    }

    @Override
    public boolean deleteLoggerOperate(String logId) {
        return this.removeById(logId);
    }

    @Override
    public boolean clearLoggerOperate() {
        return this.remove(new QueryWrapper<>());
    }

    private QueryWrapper<AgileLoggerOperate> getLoggerOperateQueryWrapper(AgileLoggerOperate agileLoggerOperate) {
        QueryWrapper<AgileLoggerOperate> queryWrapper = new QueryWrapper<>();
        if (agileLoggerOperate != null) {
            if (AgileStringUtil.isNotEmpty(agileLoggerOperate.getType())) {
                queryWrapper.lambda().eq(AgileLoggerOperate::getType, agileLoggerOperate.getType());
            }
            if (AgileStringUtil.isNotEmpty(agileLoggerOperate.getTitle())) {
                queryWrapper.lambda().like(AgileLoggerOperate::getTitle, agileLoggerOperate.getTitle());
            }
            if (AgileStringUtil.isNotEmpty(agileLoggerOperate.getStatus())) {
                queryWrapper.lambda().eq(AgileLoggerOperate::getStatus, agileLoggerOperate.getStatus());
            }
        }
        queryWrapper.lambda().orderByDesc(AgileLoggerOperate::getCreateTime);
        return queryWrapper;
    }

}
