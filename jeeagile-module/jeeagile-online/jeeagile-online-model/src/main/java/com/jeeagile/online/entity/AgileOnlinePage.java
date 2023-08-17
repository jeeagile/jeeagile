package com.jeeagile.online.entity;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2023-07-31
 * @description 在线表单 表单页面
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileOnlinePage extends AgileBaseTenantModel<AgileOnlinePage> {
    /**
     * 表单ID
     */
    @ApiModelProperty("表单ID")
    @NotNull(message = "表单ID不能为空！")
    private String formId;
    /**
     * 数据表ID
     */
    @ApiModelProperty("数据表ID")
    @NotNull(message = "数据表ID不能为空！")
    private String tableId;
    /**
     * 页面编码
     */
    @ApiModelProperty("页面编码")
    @NotNull(message = "页面编码不能为空！")
    @Size(max = 20, message = "页面编码长度最大值为20！")
    private String pageCode;

    /**
     * 页面名称
     */
    @ApiModelProperty("页面名称")
    @NotNull(message = "页面名称不能为空！")
    @Size(max = 50, message = "页面名称长度最大值为50！")
    private String pageName;

    /**
     * 页面类别 (01：弹出页面 02：跳转页面)
     */
    @ApiModelProperty("页面类别")
    @NotNull(message = "页面分类不能为空！")
    private String pageKind;

    /**
     * 页面类型 (01：查询页面 02：编辑页面 03：流程页面 04：工单列表)
     */
    @ApiModelProperty("页面类型")
    @NotNull(message = "页面类型不能为空！")
    private String pageType;

    /**
     * 表单组件JSON。
     */
    @ApiModelProperty("表单组件JSON")
    private String widgetJson;

    /**
     * 表单参数JSON。
     */
    @ApiModelProperty("表单参数JSON")
    private String paramJson;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注信息最大长度为150！")
    private String remark;
}
