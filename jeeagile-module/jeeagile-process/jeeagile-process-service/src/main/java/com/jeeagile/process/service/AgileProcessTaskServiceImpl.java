package com.jeeagile.process.service;

import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.constants.ProcessOrderStatus;
import com.jeeagile.process.entity.AgileProcessOrder;
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
    private IAgileProcessOrderService agileProcessInstanceService;

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
            AgileProcessOrder agileProcessOrder = agileProcessInstanceService.getById(agileProcessTask.getInstanceId());
            if (agileProcessOrder != null && agileProcessOrder.isNotEmptyPk()) {
                agileProcessOrder.setOrderStatus(ProcessOrderStatus.REJECTED);
                agileProcessOrder.setEndTime(new Date());
                agileProcessOrder.setUpdateNullValue();
                agileProcessInstanceService.updateById(agileProcessOrder);
            }
        }
        return this.updateById(agileProcessTask);
    }

}
