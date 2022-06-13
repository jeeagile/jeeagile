package com.jeeagile.process.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程模型
 */
@Data
public class AgileProcessModel extends AgileBaseModel<AgileProcessModel> {
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
    @NotEmpty(message = "流程表单类型不能为空！")
    private String formType;
    /**
     * 流程表单ID
     */
    private String formId;
    /**
     * 流程表单地址
     */
    private String formUrl;
    /**
     * 流程部署状态 (1:已发布 2：未发布)
     */
    private String deploymentStatus;
    /**
     * 流程部署时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date deploymentTime;
    /**
     * 备注
     */
    private String remark;
}
