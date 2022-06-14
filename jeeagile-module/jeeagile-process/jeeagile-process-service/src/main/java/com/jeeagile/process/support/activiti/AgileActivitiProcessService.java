package com.jeeagile.process.support.activiti;

import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.support.IAgileProcessService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class AgileActivitiProcessService implements IAgileProcessService {
    @Autowired
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;

    @Override
    public String processDeployment(AgileProcessModel agileProcessModel) {
        Deployment deployment = repositoryService.createDeployment()
                .addString(agileProcessModel.getId() + ".bpmn", agileProcessModel.getModelXml())
                .name(agileProcessModel.getModelName())
                .key(agileProcessModel.getModelCode())
                .deploy();
        return deployment.getId();
    }

    @Override
    public String getProcessDefinitionId(String deploymentId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        if (processDefinition == null || AgileStringUtil.isEmpty(processDefinition.getId())) {
            throw new AgileFrameException("流程定义不存在");
        }
        return processDefinition.getId();
    }

    @Override
    public boolean checkProcessDefinition(String definitionId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(definitionId);
        if (processDefinition == null) {
            throw new AgileValidateException("流程定义已不存在！");
        }
        if (processDefinition.isSuspended()) {
            throw new AgileValidateException("流程定义处于挂起状态！");
        }
        return true;
    }

    @Override
    public boolean processDefinitionActive(String definitionId) {
        return updateSuspensionState(definitionId, SuspensionState.ACTIVE);
    }

    @Override
    public boolean processDefinitionSuspend(String definitionId) {
        return updateSuspensionState(definitionId, SuspensionState.SUSPENDED);
    }

    @Override
    public String startProcessInstance(String definitionId, Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(definitionId, variables);
        if (processInstance == null || AgileStringUtil.isEmpty(processInstance.getId())) {
            throw new AgileFrameException("流程定义启动失败！");
        }
        return processInstance.getId();
    }

    /**
     * 修改流程定义激活挂起状态
     */
    private boolean updateSuspensionState(String definitionId, SuspensionState suspensionState) {
        if (AgileStringUtil.isNotEmpty(definitionId)) {
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(definitionId);
            if (processDefinition != null) {
                if (SuspensionState.ACTIVE.equals(suspensionState)) {
                    repositoryService.activateProcessDefinitionById(processDefinition.getId(), false, null);
                }
                if (SuspensionState.SUSPENDED.equals(suspensionState)) {
                    repositoryService.suspendProcessDefinitionById(processDefinition.getId(), false, null);
                }
                return true;
            } else {
                throw new AgileValidateException("流程定义不存在！");
            }
        } else {
            throw new AgileValidateException("流程定义ID不能为空！");
        }
    }
}
