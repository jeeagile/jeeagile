package com.jeeagile.frame.entity.online;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2023-07-19
 * @description 在线表单 表单基础信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileOnlineForm extends AgileBaseTenantModel<AgileOnlineForm> {
    /**
     * 表单编码
     */
    @ApiModelProperty("表单编码")
    @NotNull(message = "表单编码不能为空！")
    @Size(max = 20, message = "表单编码长度最大值为20！")
    private String formCode;

    /**
     * 表单名称
     */
    @ApiModelProperty("表单名称")
    @NotNull(message = "表单名称不能为空！")
    @Size(max = 50, message = "表单名称长度最大值为50！")
    private String formName;

    /**
     * 表单类型（01:业务表单 02:流程表单）
     */
    @ApiModelProperty("表单类型")
    @NotNull(message = "表单类型不能为空！")
    private String formType;

    /**
     * 表单状态（01:编辑基础信息 02：编辑数据模型 03：表单页面设计）
     */
    @ApiModelProperty("表单状态")
    private String formStatus;

    /**
     * 表单状态（1:已发布 2：未发布）
     */
    @ApiModelProperty("发布状态")
    private String publishStatus;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注信息最大长度为150！")
    private String remark;
}
