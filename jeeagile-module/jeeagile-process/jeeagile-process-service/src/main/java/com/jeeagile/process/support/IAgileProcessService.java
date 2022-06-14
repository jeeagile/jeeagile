package com.jeeagile.process.support;

import com.jeeagile.process.entity.AgileProcessModel;

import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-02
 * @description 流程操作接口（对接流程组件）
 */
public interface IAgileProcessService {
    /**
     * 流程发布
     *
     * @param agileProcessModel
     * @return
     */
    String processDeployment(AgileProcessModel agileProcessModel);

    /**
     * 通过流程部署ID获取流程定义ID
     *
     * @param deploymentId
     * @return
     */
    String getProcessDefinitionId(String deploymentId);

    /**
     * 校验流程定义
     *
     * @param definitionId
     * @return
     */
    boolean checkProcessDefinition(String definitionId);

    /**
     * 流程定义激活
     *
     * @param definitionId
     * @return
     */
    boolean processDefinitionActive(String definitionId);

    /**
     * 流程定义挂起
     *
     * @param definitionId
     * @return
     */
    boolean processDefinitionSuspend(String definitionId);

    /**
     * 启动流程实例
     *
     * @param definitionId
     * @param variables
     * @return
     */
    String startProcessInstance(String definitionId, Map<String, Object> variables);
}
