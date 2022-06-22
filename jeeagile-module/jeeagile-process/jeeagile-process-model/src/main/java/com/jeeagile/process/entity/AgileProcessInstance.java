package com.jeeagile.process.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.frame.entity.AgileBaseModel;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-06
 * @description 流程模型
 */
@Data
public class AgileProcessInstance extends AgileBaseModel<AgileProcessInstance> {
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
     * 流程模型版本
     */
    private int modelVersion;
    /**
     * 流程模型设计XMl
     */
    private String modelXml;
    /**
     * 流程表单类型（1:流程表单 2:业务表单）
     */
    private String formType;
    /**
     * 流程表单名称
     */
    private String formName;
    /**
     * 表单配置
     */
    private String formConf;
    /**
     * 表单字段
     */
    private String formFields;
    /**
     * 流程表单地址
     */
    private String formUrl;
    /**
     * 表单数据
     */
    private String formData;
    /**
     * 流程部署时间
     */
    private Date deploymentTime;
    /**
     * 流程定义ID
     */
    private String definitionId;
    /**
     * 实例挂起状态(1:激活，2:挂起)
     */
    private int suspensionState;
    /**
     * 流程实例状态(0:已撤销 1:办理中 2:已完成)
     */
    private String instanceStatus;
    /**
     * 流程实例发起人员
     */
    private String startUser;
    /**
     * 流程实例发起人员名称
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

    /**
     * 流程实例高亮线
     */
    @TableField(exist = false)
    private Map<String, Object> highLineData;

}
