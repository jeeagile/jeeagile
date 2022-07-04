package com.jeeagile.process.entity;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import lombok.Data;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2022-06-17
 * @description 流程任务
 */
@Data
public class AgileProcessTask extends AgileBaseTenantModel<AgileProcessTask> {
    /**
     * 流程模型编码
     */
    private String modelId;
    /**
     * 流程模型编码
     */
    private String modelCode;
    /**
     * 流程模型名称
     */
    private String modelName;
    /**
     * 流程表单名称
     */
    private String formName;
    /**
     * 流程定义ID（流程组件生成）
     */
    private String definitionId;
    /**
     * 流程实例ID（流程组件生成）
     */
    private String instanceId;
    /**
     * 流程任务名称
     */
    private String taskName;
    /**
     * 流程任务执行人
     */
    private String taskUser;
    /**
     * 流程任务执行人名称
     */
    private String taskUserName;
    /**
     * 流程任务状态(0:用户撤销 1:审批中 2：审批通过 3：审批拒绝)
     */
    private String taskStatus;
    /**
     * 审批意见
     */
    private String approveMessage;
    /**
     * 流程发起人
     */
    private String startUser;
    /**
     * 流程发起人名称
     */
    private String startUserName;
    /**
     * 流程实例启动时间
     */
    private Date startTime;
    /**
     * 流程实例结束时间
     */
    private Date endTime;
}
