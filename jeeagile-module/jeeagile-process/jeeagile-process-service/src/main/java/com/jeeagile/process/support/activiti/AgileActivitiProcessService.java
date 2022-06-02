package com.jeeagile.process.support.activiti;

import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.support.IAgileProcessService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AgileActivitiProcessService implements IAgileProcessService {
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public AgileProcessModel processDeployment(AgileProcessModel agileProcessModel) {
        Deployment deployment = repositoryService.createDeployment()
                .addString(agileProcessModel.getId() + ".bpmn", agileProcessModel.getProcessXml())
                .name(agileProcessModel.getModelName())
                .key(agileProcessModel.getModelCode())
                .deploy();
        agileProcessModel.setDeploymentId(deployment.getId());
        agileProcessModel.setDeploymentTime(new Date());
        agileProcessModel.setDeploymentStatus("1");
        return agileProcessModel;
    }

    @Override
    public String getProcessDefinitionId(String deploymentId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        return processDefinition.getId();
    }
}
