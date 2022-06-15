package com.jeeagile.process.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.util.AgileBeanUtils;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.support.IAgileProcessService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-10
 * @description 我的事务
 */
@AgileService
public class AgileProcessTaskServiceImpl implements IAgileProcessTaskService {
    @Autowired
    private IAgileProcessDefinitionService agileProcessDefinitionService;
    @Autowired
    private IAgileProcessInstanceService agileProcessInstanceService;
    @Autowired
    private IAgileProcessService agileProcessService;

    @Override
    public AgilePage<AgileProcessDefinition> selectProcessDefinitionPage(AgilePageable<AgileProcessDefinition> agilePageable) {
        AgilePage<AgileProcessDefinition> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileProcessDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessDefinition.class, i -> !"modelXml".contains(i.getProperty()) || !"formFields".contains(i.getProperty()) || !"formConfig".contains(i.getProperty()));
        AgileProcessDefinition agileProcessDefinition = agilePageable.getQueryCond();
        if (agileProcessDefinition != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getModelCode())) {
                lambdaQueryWrapper.eq(AgileProcessDefinition::getModelCode, agileProcessDefinition.getModelCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getModelName())) {
                lambdaQueryWrapper.like(AgileProcessDefinition::getModelName, agileProcessDefinition.getModelName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getFormName())) {
                lambdaQueryWrapper.like(AgileProcessDefinition::getFormName, agileProcessDefinition.getFormName());
            }
        }
        lambdaQueryWrapper.eq(AgileProcessDefinition::getSuspensionState, 1);
        lambdaQueryWrapper.eq(AgileProcessDefinition::getMainVersion, 1);
        lambdaQueryWrapper.orderByDesc(AgileProcessDefinition::getModelCode, AgileProcessDefinition::getModelVersion);
        return agileProcessDefinitionService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgileProcessDefinition selectProcessDefinitionInfo(String processDefinitionId) {
        return agileProcessDefinitionService.selectModel(processDefinitionId);
    }

    @Override
    public boolean startProcessInstance(String processDefinitionId, Map<String, Object> formData) {
        AgileProcessDefinition agileProcessDefinition = agileProcessDefinitionService.selectModel(processDefinitionId);
        if (agileProcessDefinition == null || agileProcessDefinition.isEmptyPk()) {
            throw new AgileValidateException("流程定义已不存在！");
        }
        if (!agileProcessService.checkProcessDefinition(agileProcessDefinition.getDefinitionId())) {
            throw new AgileValidateException("流程定义校验未通过，不能发起流程！");
        }
        String instanceId = agileProcessService.startProcessInstance(agileProcessDefinition.getDefinitionId(), formData);
        if (AgileStringUtil.isEmpty(instanceId)) {
            throw new AgileFrameException("流程启动失败！");
        }
        AgileProcessInstance agileProcessInstance = new AgileProcessInstance();
        AgileBeanUtils.copyProperties(agileProcessDefinition, agileProcessInstance);
        agileProcessInstance.setInstanceId(instanceId);
        agileProcessInstance.setStartUser(AgileSecurityUtil.getUserId());
        agileProcessInstance.setStartTime(new Date());
        agileProcessInstance.setFormData(JSON.toJSONString(formData));
        agileProcessInstance.setInstanceStatus("1");
        agileProcessInstanceService.saveModel(agileProcessInstance);
        return true;
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
        lambdaQueryWrapper.eq(AgileProcessInstance::getStartUser, AgileSecurityUtil.getUserId());
        lambdaQueryWrapper.orderByDesc(AgileProcessInstance::getStartTime);
        return agileProcessInstanceService.page(agilePage, lambdaQueryWrapper);
    }


}
