package com.jeeagile.process.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程模型
 */
@Data
public class AgileProcessModel extends AgileBaseModel<AgileProcessModel> {
    /**
     * 流程编码
     */
    @NotEmpty(message = "流程编码不能为空！")
    @Size(max = 10, message = "流程编码最大长度为10！")
    private String processCode;
    /**
     * 流程名称
     */
    @NotEmpty(message = "流程名称不能为空！")
    @Size(max = 50, message = "流程名称最大长度为50！")
    private String processName;
    /**
     * 流程版本
     */
    private int processVersion;
    /**
     * 流程设计XMl
     */
    private String processDesigner;
    /**
     * 流程表单类型
     */
    @NotEmpty(message = "流程表单类型不能为空！")
    private String processFormType;
    /**
     * 流程表单ID
     */
    private String processFormId;
    /**
     * 流程表单地址
     */
    private String processFormUrl;
    /**
     * 流程部署状态
     */
    private String processDeploymentStatus;
    /**
     * 流程部署时间
     */
    private String processDeploymentTime;
    /**
     * 流程部署ID（流程组件生成）
     */
    private String processDeploymentId;
    /**
     * 备注
     */
    private String remark;
}
