package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessInstance;
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
    @Autowired
    private IAgileProcessInstanceService agileProcessInstanceService;

    @Override
    public AgilePage<AgileProcessTask> selectTodo(AgilePageable<AgileProcessTask> agilePageable) {
        return agileProcessService.getUserTodoTask(agilePageable);
    }

    @Override
    public AgilePage<AgileProcessTask> selectDone(AgilePageable<AgileProcessTask> agilePageable) {
        return agileProcessService.getUserDoneTask(agilePageable);
    }

    @Override
    public boolean approveProcessTask(AgileProcessTask agileProcessTask) {
        return handlerProcessTask(agileProcessTask, true);
    }

    @Override
    public boolean refuseProcessTask(AgileProcessTask agileProcessTask) {
        return handlerProcessTask(agileProcessTask, false);
    }

    public boolean handlerProcessTask(AgileProcessTask agileProcessTask, boolean flag) {
        String approveMessage = agileProcessTask.getApproveMessage();
        agileProcessTask = this.getById(agileProcessTask.getId());
        if (agileProcessTask == null || agileProcessTask.isEmptyPk()) {
            throw new AgileFrameException("流程任务已不存在！");
        }
        agileProcessTask.setApproveMessage(approveMessage);
        agileProcessTask.setUpdateNullValue();
        agileProcessTask.setEndTime(new Date());
        if (flag) {
            agileProcessTask.setTaskStatus("2");
            agileProcessService.approveProcessTask(agileProcessTask.getInstanceId(), agileProcessTask.getId(), approveMessage);
        } else {
            agileProcessTask.setTaskStatus("3");
            agileProcessService.refuseProcessTask(agileProcessTask.getInstanceId(), agileProcessTask.getId(), approveMessage);
            AgileProcessInstance agileProcessInstance = agileProcessInstanceService.getById(agileProcessTask.getInstanceId());
            if (agileProcessInstance != null && agileProcessInstance.isNotEmptyPk()) {
                agileProcessInstance.setInstanceStatus("3");
                agileProcessInstance.setEndTime(new Date());
                agileProcessInstance.setUpdateNullValue();
                agileProcessInstanceService.updateById(agileProcessInstance);
            }
        }
        return this.updateById(agileProcessTask);
    }

}
