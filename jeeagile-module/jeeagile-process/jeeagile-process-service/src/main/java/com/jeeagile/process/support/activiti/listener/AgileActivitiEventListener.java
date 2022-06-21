package com.jeeagile.process.support.activiti.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import com.jeeagile.frame.user.AgileUserData;
import com.jeeagile.frame.util.AgileBeanUtils;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessForm;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.entity.AgileProcessTask;
import com.jeeagile.process.service.IAgileProcessDefinitionService;
import com.jeeagile.process.service.IAgileProcessInstanceService;
import com.jeeagile.process.service.IAgileProcessTaskService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiActivityCancelledEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiProcessCancelledEventImpl;
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
    private IAgileProcessInstanceService agileProcessInstanceService;
    @Lazy
    @Autowired
    private IAgileProcessTaskService agileProcessTaskService;
    @Lazy
    @Autowired
    private IAgileSysUserService agileSysUserService;

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
                break;
            case ACTIVITY_CANCELLED:
                log.debug("节点取消");
                break;
            //任务开始
            case TASK_CREATED:
                log.debug("任务开始");
                this.taskCreated((ActivitiEntityEventImpl) activitiEvent);
                break;
            //任务完成
            case TASK_COMPLETED:
                log.debug("任务完成");
                break;
            //进程取消，删除实例
            case PROCESS_CANCELLED:
                log.debug("进程取消，删除实例");
                this.processCancelled((ActivitiProcessCancelledEventImpl) activitiEvent);
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
        AgileProcessInstance agileProcessInstance = new AgileProcessInstance();
        AgileBeanUtils.copyProperties(agileProcessDefinition, agileProcessInstance);
        agileProcessInstance.setId(processStartedEvent.getProcessInstanceId());
        agileProcessInstance.setDefinitionId(agileProcessDefinition.getId());
        AgileUserData agileUserData = AgileSecurityUtil.getUserData();
        agileProcessInstance.setStartUser(agileUserData.getUserId());
        agileProcessInstance.setStartUserName(agileUserData.getNickName());
        agileProcessInstance.setStartTime(new Date());
        agileProcessInstance.setFormData(JSON.toJSONString(processStartedEvent.getVariables()));
        agileProcessInstance.setInstanceStatus("1");
        agileProcessInstanceService.saveModel(agileProcessInstance);
    }

    private void taskCreated(ActivitiEntityEventImpl activitiEntityEvent) {
        AgileProcessInstance agileProcessInstance = agileProcessInstanceService.getById(activitiEntityEvent.getProcessInstanceId());
        if (agileProcessInstance == null || agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例不存在！");
        }
        AgileProcessTask agileProcessTask = new AgileProcessTask();
        AgileBeanUtils.copyProperties(agileProcessInstance, agileProcessTask);
        agileProcessTask.setInstanceId(agileProcessInstance.getId());
        Task task = (Task) activitiEntityEvent.getEntity();
        agileProcessTask.setId(task.getId());
        agileProcessTask.setTaskName(task.getName());
        agileProcessTask.setStartTime(task.getCreateTime());
        AgileSysUser agileSysUser = agileSysUserService.getById(task.getAssignee());
        if (agileSysUser == null || agileSysUser.isEmptyPk()) {
            throw new AgileFrameException("流程任务执行人不存在，请核实!");
        }
        agileProcessTask.setTaskUser(agileSysUser.getId());
        agileProcessTask.setTaskUserName(agileSysUser.getNickName());
        agileProcessTask.setTaskStatus("1");
        agileProcessTaskService.saveModel(agileProcessTask);
    }

    private void taskCompleted() {

    }

    private void processCancelled(ActivitiProcessCancelledEventImpl processCancelledEvent) {
        AgileProcessInstance agileProcessInstance = agileProcessInstanceService.getById(processCancelledEvent.getProcessInstanceId());
        if (agileProcessInstance == null || agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例不存在！");
        }
        agileProcessInstance.setInstanceStatus("0");
        agileProcessInstance.setEndTime(new Date());
        agileProcessInstance.setUpdateValue();
        agileProcessInstanceService.updateById(agileProcessInstance);
    }
}
