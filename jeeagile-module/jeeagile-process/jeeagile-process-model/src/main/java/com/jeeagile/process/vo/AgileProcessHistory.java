package com.jeeagile.process.vo;

import com.jeeagile.frame.vo.AgileBaseVo;
import lombok.Data;

import java.util.Date;

/**
 * 流程历史审批信息
 */
@Data
public class AgileProcessHistory extends AgileBaseVo {
    /**
     * 节点ID
     */
    private String activityId;

    /**
     * 节点名称
     */
    private String activityName;

    /**
     * 节点类型
     */
    private String activityType;

    /**
     * 流程定义ID
     */
    private String definitionId;

    /**
     * 流程实例ID
     */
    private String instanceId;

    /**
     *
     */
    private String executionId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 执行人
     */
    private String assignee;

    /**
     * 执行人名称
     */
    private String assigneeName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 执行时间（毫秒）
     */
    private Long durationInMillis;

    /**
     * 执行时间
     */
    private String durationTime;

    /**
     * 审批信息
     */
    private String message;

    /**
     * 审批状态
     */
    private String status;
}
