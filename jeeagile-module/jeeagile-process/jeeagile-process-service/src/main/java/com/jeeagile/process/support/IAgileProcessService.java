package com.jeeagile.process.support;

import com.jeeagile.process.entity.AgileProcessModel;

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
    AgileProcessModel processDeployment(AgileProcessModel agileProcessModel);

    /**
     * 通过流程部署ID获取流程定义ID
     *
     * @param deploymentId
     * @return
     */
    String getProcessDefinitionId(String deploymentId);

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
}
