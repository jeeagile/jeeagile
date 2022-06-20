package com.jeeagile.process.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2022-06-06
 * @description 流程模型
 */
@Data
public class AgileProcessDefinition extends AgileBaseModel<AgileProcessDefinition> {
    /**
     * 流程模型编码
     */
    @NotEmpty(message = "流程模型ID不能为空！")
    private String modelId;
    /**
     * 流程模型编码
     */
    @NotEmpty(message = "流程模型编码不能为空！")
    @Size(max = 10, message = "流程模型编码最大长度为10！")
    private String modelCode;
    /**
     * 流程模型名称
     */
    @NotEmpty(message = "流程模型名称不能为空！")
    @Size(max = 50, message = "流程模型名称最大长度为50！")
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
     * 流程定义主版本（1：主版本 2：非主版本）
     */
    private int mainVersion;
    /**
     * 流程部署时间
     */
    private Date deploymentTime;
    /**
     * 流程挂起状态(1:激活，2:挂起)
     */
    private int suspensionState;
}
