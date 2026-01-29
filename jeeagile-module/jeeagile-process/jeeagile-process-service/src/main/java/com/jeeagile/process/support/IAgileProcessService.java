package com.jeeagile.process.support;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.entity.AgileProcessTask;
import com.jeeagile.process.vo.AgileProcessHistory;

import java.util.List;
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
     * @return 返回流程实例ID
     */
    String startProcessInstance(String definitionId, Map<String, Object> variables);


    /**
     * 获取流程高亮线
     *
     * @param definitionId 流程定义ID
     * @param instanceId   流程实例ID
     * @return
     */
    Map<String, Object> getProcessInstanceHighLineData(String definitionId, String instanceId);

    /**
     * 获取流程历史审批信息
     *
     * @param instanceId
     * @return
     */
    List<AgileProcessHistory> getProcessInstanceHistoric(String instanceId);

    /**
     * 获取用户代办列表
     *
     * @return
     */
    AgilePage<AgileProcessTask> getUserTodoTask(AgilePageable<AgileProcessTask> agilePageable);

    /**
     * 获取用户代办列表
     *
     * @return
     */
    AgilePage<AgileProcessTask> getUserDoneTask(AgilePageable<AgileProcessTask> agilePageable);

    /**
     * 撤销流程实例
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 撤销原因
     * @return
     */
    boolean cancelProcessInstance(String instanceId, String deleteReason);


    /**
     * 审批流程
     *
     * @param instanceId
     * @param taskId
     * @param approveMessage
     * @return
     */
    boolean approveProcessTask(String instanceId, String taskId, String approveMessage);

    /**
     * 审核驳回
     *
     * @param instanceId
     * @param taskId
     * @param approveMessage
     * @return
     */
    boolean refuseProcessTask(String instanceId, String taskId, String approveMessage);
}
