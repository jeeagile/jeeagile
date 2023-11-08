package com.jeeagile.process.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.mapper.AgileProcessInstanceMapper;
import com.jeeagile.process.support.IAgileProcessService;
import com.jeeagile.process.vo.AgileProcessHistory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 流程实例
 */
@AgileService
public class AgileProcessInstanceServiceImpl extends AgileBaseServiceImpl<AgileProcessInstanceMapper, AgileProcessInstance> implements IAgileProcessInstanceService {

    @Autowired
    private IAgileProcessDefinitionService agileProcessDefinitionService;
    @Autowired
    private IAgileProcessService agileProcessService;
    @Autowired
    private IAgileSysUserService agileSysUserService;

    @Override
    public boolean startProcessInstance(String processDefinitionId, Map<String, Object> formData) {
        AgileProcessDefinition agileProcessDefinition = agileProcessDefinitionService.selectModel(processDefinitionId);
        if (agileProcessDefinition == null || agileProcessDefinition.isEmptyPk()) {
            throw new AgileValidateException("流程定义已不存在！");
        }
        if (!agileProcessService.checkProcessDefinition(agileProcessDefinition.getId())) {
            throw new AgileValidateException("流程定义校验未通过，不能发起流程！");
        }
        return agileProcessService.startProcessInstance(agileProcessDefinition.getId(), formData);
    }

    @Override
    public AgilePage<AgileProcessInstance> selectProcessInstancePage(AgilePageable<AgileProcessInstance> agilePageable) {
        AgilePage<AgileProcessInstance> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileProcessInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessInstance.class, i -> !"modelXml".contains(i.getProperty()) || !"formFields".contains(i.getProperty()) || !"formConfig".contains(i.getProperty()));
        AgileProcessInstance agileProcessInstance = agilePageable.getQueryCond();
        if (agileProcessInstance != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessInstance.getModelCode())) {
                lambdaQueryWrapper.eq(AgileProcessInstance::getModelCode, agileProcessInstance.getModelCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessInstance.getInstanceStatus())) {
                lambdaQueryWrapper.like(AgileProcessInstance::getInstanceStatus, agileProcessInstance.getInstanceStatus());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessInstance.getModelName())) {
                lambdaQueryWrapper.like(AgileProcessInstance::getModelName, agileProcessInstance.getModelName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessInstance.getFormName())) {
                lambdaQueryWrapper.like(AgileProcessInstance::getFormName, agileProcessInstance.getFormName());
            }
        }
        lambdaQueryWrapper.eq(AgileProcessInstance::getStartUser, AgileSecurityContext.getUserId());
        lambdaQueryWrapper.orderByDesc(AgileProcessInstance::getStartTime);
        return this.page(agilePage, lambdaQueryWrapper);
    }


    @Override
    public AgileProcessInstance selectProcessInstanceInfo(String processInstanceId) {
        AgileProcessInstance agileProcessInstance = this.getById(processInstanceId);
        if (agileProcessInstance == null || agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        agileProcessInstance.setHighLineData(agileProcessService.getProcessInstanceHighLineData(agileProcessInstance.getDefinitionId(), processInstanceId));
        return agileProcessInstance;
    }

    @Override
    public List<AgileProcessHistory> selectProcessInstanceHistory(String processInstanceId) {
        AgileProcessInstance agileProcessInstance = this.getById(processInstanceId);
        if (agileProcessInstance == null || agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        List<AgileProcessHistory> agileProcessHistoryList = agileProcessService.getProcessInstanceHistoric(processInstanceId);
        agileProcessHistoryList.forEach(agileProcessHistory -> {
            //执行人
            String assignee = agileProcessHistory.getAssignee();
            if (agileProcessHistory.getActivityType().equals("startEvent")) {
                assignee = agileProcessInstance.getStartUser();
            }
            if (AgileStringUtil.isNotEmpty(assignee)) {
                AgileSysUser agileSysUser = agileSysUserService.getById(assignee);
                if (agileSysUser != null && agileSysUser.isNotEmptyPk()) {
                    agileProcessHistory.setAssigneeName(agileSysUser.getNickName());
                }
            }
        });
        return agileProcessHistoryList;
    }

    @Override
    public boolean cancelProcessInstance(String processInstanceId) {
        AgileProcessInstance agileProcessInstance = this.getById(processInstanceId);
        if (agileProcessInstance == null || agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        agileProcessService.cancelProcessInstance(processInstanceId, "发起人撤销流程");
        agileProcessInstance.setInstanceStatus("0");
        agileProcessInstance.setEndTime(new Date());
        agileProcessInstance.setUpdateNullValue();
        return this.updateById(agileProcessInstance);
    }
}
