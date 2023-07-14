package com.jeeagile.process.entity;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author JeeAgile
 * @date 2022-08-08
 * @description 流程表达式
 */
@Data
public class AgileProcessExpression extends AgileBaseTenantModel<AgileProcessExpression> {
    /**
     * 流程表达式编码
     */
    @NotNull(message = "流程表达式编码不能为空！")
    @Size(max = 20, message = "流程表达式编码最大长度为20！")
    private String expressionCode;
    /**
     * 流程表达式名称
     */
    @NotNull(message = "流程表达式名称不能为空！")
    @Size(max = 50, message = "流程表达式名称最大长度为50！")
    private String expressionName;

    /**
     * 流程表达式键值
     */
    @NotNull(message = "流程表达式不能为空！")
    @Size(max = 150, message = "流程表达式最大长度为150！")
    private String expressionValue;
    /**
     * 流程表达式状态 （0:正常 1:停用）
     */
    @NotEmpty(message = "流程表达式状态不能为空！")
    private String expressionStatus;
    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注信息最大长度为150！")
    private String remark;
}
