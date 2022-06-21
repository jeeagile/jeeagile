package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessTask;
import com.jeeagile.process.mapper.AgileProcessTaskMapper;

/**
 * @author JeeAgile
 * @date 2022-06-10
 * @description 我的事务
 */
@AgileService
public class AgileProcessTaskServiceImpl extends AgileBaseServiceImpl<AgileProcessTaskMapper, AgileProcessTask> implements IAgileProcessTaskService {
    @Override
    public AgilePage<AgileProcessTask> selectTodo(AgilePageable<AgileProcessTask> agilePageable) {
        AgilePage<AgileProcessTask> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileProcessTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        AgileProcessTask agileProcessTask = agilePageable.getQueryCond();
        if (agileProcessTask != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessTask.getModelCode())) {
                lambdaQueryWrapper.eq(AgileProcessTask::getModelCode, agileProcessTask.getModelCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessTask.getStartUserName())) {
                lambdaQueryWrapper.like(AgileProcessTask::getStartUserName, agileProcessTask.getStartUserName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessTask.getModelName())) {
                lambdaQueryWrapper.like(AgileProcessTask::getModelName, agileProcessTask.getModelName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessTask.getFormName())) {
                lambdaQueryWrapper.like(AgileProcessTask::getFormName, agileProcessTask.getFormName());
            }
        }
        lambdaQueryWrapper.eq(AgileProcessTask::getTaskUser, AgileSecurityUtil.getUserId());
        lambdaQueryWrapper.orderByDesc(AgileProcessTask::getCreateTime);
        return this.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileProcessTask> selectDone(AgilePageable<AgileProcessTask> agilePageable) {
        return null;
    }

    @Override
    public AgileProcessTask approveProcessTask(AgileProcessTask agileProcessTask) {
        return null;
    }

    @Override
    public AgileProcessTask rejectProcessTask(AgileProcessTask agileProcessTask) {
        return null;
    }


}
