package com.jeeagile.process.support.activiti.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import com.jeeagile.frame.user.AgileUserData;
import com.jeeagile.frame.util.AgileBeanUtils;
import com.jeeagile.process.constants.ProcessFormType;
import com.jeeagile.process.constants.ProcessOrderStatus;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessOrder;
import com.jeeagile.process.entity.AgileProcessTask;
import com.jeeagile.process.service.IAgileProcessDefinitionService;
import com.jeeagile.process.service.IAgileProcessOrderService;
import com.jeeagile.process.service.IAgileProcessTaskService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiProcessStartedEventImpl;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class AgileActivitiEventListener implements ActivitiEventListener {

    @Lazy
    @Autowired
    private IAgileProcessDefinitionService agileProcessDefinitionService;
    @Lazy
    @Autowired
    private IAgileProcessOrderService agileProcessInstanceService;
    @Lazy
    @Autowired
    private IAgileProcessTaskService agileProcessTaskService;
    @Lazy
    @Autowired
    private IAgileSysUserService agileSysUserService;

    @Lazy
    @Autowired
    private TaskService taskService;


    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        switch (activitiEvent.getType()) {
            //流程开始
            case PROCESS_STARTED:
                log.debug("流程开始");
                this.processStarted((ActivitiProcessStartedEventImpl) activitiEvent);
                break;
            //流程结束
            case PROCESS_COMPLETED:
                log.debug("流程结束");
                this.processCompleted((ActivitiEntityEventImpl) activitiEvent);
                break;

            case ACTIVITY_CANCELLED:
                log.debug("节点取消");
                break;
            //任务开始
            case TASK_CREATED:
                log.debug("任务开始");
                this.taskCreated((ActivitiEntityEventImpl) activitiEvent);
                break;
            //进程取消，删除实例
            case PROCESS_CANCELLED:
                log.debug("进程取消，删除实例");
//                this.processCancelled((ActivitiProcessCancelledEventImpl) activitiEvent);
            default:
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }


    private void processStarted(ActivitiProcessStartedEventImpl processStartedEvent) {
        AgileProcessDefinition agileProcessDefinition = agileProcessDefinitionService.getById(processStartedEvent.getProcessDefinitionId());
        if (agileProcessDefinition == null || agileProcessDefinition.isEmptyPk()) {
            throw new AgileFrameException("流程定义不存在！");
        }
        AgileProcessOrder agileProcessOrder = new AgileProcessOrder();
        AgileBeanUtils.copyProperties(agileProcessDefinition, agileProcessOrder);
        agileProcessOrder.setDefinitionId(agileProcessDefinition.getId());
        agileProcessOrder.setInstanceId(processStartedEvent.getProcessInstanceId());
        AgileUserData agileUserData = (AgileUserData) AgileSecurityContext.getUserData();
        agileProcessOrder.setStartUser(agileUserData.getUserId());
        agileProcessOrder.setStartUserName(agileUserData.getNickName());
        agileProcessOrder.setStartTime(new Date());
        agileProcessOrder.setPageId(agileProcessDefinition.getPageId());
        agileProcessOrder.setId(null);
        if (ProcessFormType.PROCESS_FORM.equals(agileProcessDefinition.getFormType())) {
            agileProcessOrder.setFormData(JSON.toJSONString(processStartedEvent.getVariables()));
        } else if (ProcessFormType.ONLINE_FORM.equals(agileProcessDefinition.getFormType())) {
            agileProcessOrder.setPageKey((String) processStartedEvent.getVariables().get("pageKey"));
        }
        agileProcessOrder.setOrderStatus(ProcessOrderStatus.SUBMITTED);
        agileProcessInstanceService.saveModel(agileProcessOrder);
    }

    private void taskCreated(ActivitiEntityEventImpl activitiEntityEvent) {
        LambdaQueryWrapper<AgileProcessOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileProcessOrder::getInstanceId, activitiEntityEvent.getProcessInstanceId());
        AgileProcessOrder agileProcessOrder = agileProcessInstanceService.getOne(lambdaQueryWrapper);
        if (agileProcessOrder == null || agileProcessOrder.isEmptyPk()) {
            throw new AgileFrameException("流程实例不存在！");
        }
        Task task = (Task) activitiEntityEvent.getEntity();

        AgileProcessTask agileProcessTask = new AgileProcessTask();
        AgileBeanUtils.copyProperties(agileProcessOrder, agileProcessTask);
        agileProcessTask.setInstanceId(agileProcessOrder.getInstanceId());
        agileProcessTask.setId(task.getId());
        agileProcessTask.setOrderId(agileProcessOrder.getId());
        agileProcessTask.setTaskName(task.getName());
        agileProcessTask.setStartTime(task.getCreateTime());


        if (AgileStringUtil.isNotEmpty(task.getAssignee())) {
            AgileSysUser agileSysUser = agileSysUserService.getById(task.getAssignee());
            if (agileSysUser == null || agileSysUser.isEmptyPk()) {
                throw new AgileFrameException("流程任务执行人不存在，请联系管理员核实!");
            }
            agileProcessTask.setAssigneeUser(agileSysUser.getId());
            agileProcessTask.setAssigneeUserName(agileSysUser.getNickName());
        }

//        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
//        if (AgileCollectionUtil.isNotEmpty(identityLinkList)) {
//            identityLinkList.forEach(identityLink -> {
//                System.out.println(identityLink.getType() + " " + identityLink.getUserId() + "  " + identityLink.getGroupId());
//            });
//        }


        agileProcessTask.setTaskStatus("1");
        agileProcessTaskService.saveModel(agileProcessTask);

        LambdaUpdateWrapper<AgileProcessOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AgileProcessOrder::getId, agileProcessOrder.getId());
        lambdaUpdateWrapper.set(AgileProcessOrder::getTaskId, agileProcessTask.getId());
        agileProcessInstanceService.update(lambdaUpdateWrapper);
    }

    private void processCompleted(ActivitiEntityEventImpl activitiEntityEvent) {
        AgileProcessOrder agileProcessOrder = agileProcessInstanceService.getById(activitiEntityEvent.getProcessInstanceId());
        agileProcessOrder.setOrderStatus(ProcessOrderStatus.FINISHED);
        agileProcessOrder.setEndTime(new Date());
        agileProcessOrder.setUpdateNullValue();
        agileProcessInstanceService.updateById(agileProcessOrder);
    }

//    private void processCancelled(ActivitiProcessCancelledEventImpl processCancelledEvent) {
//        AgileProcessInstance agileProcessInstance = agileProcessInstanceService.getById(processCancelledEvent.getProcessInstanceId());
//        agileProcessInstance.setInstanceStatus("0");
//        agileProcessInstance.setEndTime(new Date());
//        agileProcessInstance.setUpdateNullValue();
//        agileProcessInstanceService.updateById(agileProcessInstance);
//    }
}
