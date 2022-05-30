package com.jeeagile.process.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程表单
 */
@Data
public class AgileProcessForm extends AgileBaseModel<AgileProcessForm> {
    /**
     * 表单编码
     */
    @NotEmpty(message = "表单编码不能为空！")
    @Size(max = 10, message = "表单编码最大长度为10！")
    private String formCode;
    /**
     * 表单名称
     */
    @NotEmpty(message = "表单名称不能为空！")
    @Size(max = 50, message = "任务名称最大长度为10！")
    private String formName;
    /**
     * 表单状态
     */
    @NotEmpty(message = "表单状态不能为空！")
    private String formStatus;
    /**
     * 表单配置
     */
    private String formConfig;
    /**
     * 表单字段
     */
    private String formFields;
    /**
     * 备注
     */
    private String remark;
}
