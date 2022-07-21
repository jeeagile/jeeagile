package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessTask;
import com.jeeagile.process.mapper.AgileProcessTaskMapper;
import com.jeeagile.process.support.IAgileProcessService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2022-06-10
 * @description 我的事务
 */
@AgileService
public class AgileProcessTaskServiceImpl extends AgileBaseServiceImpl<AgileProcessTaskMapper, AgileProcessTask> implements IAgileProcessTaskService {
    @Autowired
    private IAgileProcessService agileProcessService;

    @Override
    public AgilePage<AgileProcessTask> selectTodo(AgilePageable<AgileProcessTask> agilePageable) {
        return selectProcessTaskPage(agilePageable, true);
    }

    @Override
    public AgilePage<AgileProcessTask> selectDone(AgilePageable<AgileProcessTask> agilePageable) {
        return selectProcessTaskPage(agilePageable, false);
    }

    private AgilePage<AgileProcessTask> selectProcessTaskPage(AgilePageable<AgileProcessTask> agilePageable, boolean todoFlag) {
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
        if (todoFlag) {
            lambdaQueryWrapper.eq(AgileProcessTask::getTaskStatus, "1");
            lambdaQueryWrapper.isNull(AgileProcessTask::getEndTime);
        } else {
            lambdaQueryWrapper.in(AgileProcessTask::getTaskStatus, "2", "3");
        }
        lambdaQueryWrapper.eq(AgileProcessTask::getAssigneeUser, AgileSecurityContext.getUserId());
        lambdaQueryWrapper.orderByDesc(AgileProcessTask::getCreateTime);
        return this.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public boolean approveProcessTask(AgileProcessTask agileProcessTask) {
        String approveMessage = agileProcessTask.getApproveMessage();
        agileProcessTask = this.getById(agileProcessTask.getId());
        if (agileProcessTask == null || agileProcessTask.isEmptyPk()) {
            throw new AgileFrameException("流程任务已不存在！");
        }
        agileProcessTask.setApproveMessage(approveMessage);
        agileProcessTask.setUpdateNullValue();
        agileProcessTask.setEndTime(new Date());
        agileProcessTask.setTaskStatus("2");
        agileProcessService.approveProcessTask(agileProcessTask.getInstanceId(), agileProcessTask.getId(), approveMessage);
        return this.updateById(agileProcessTask);
    }

    @Override
    public boolean rejectProcessTask(AgileProcessTask agileProcessTask) {
        return false;
    }

}
