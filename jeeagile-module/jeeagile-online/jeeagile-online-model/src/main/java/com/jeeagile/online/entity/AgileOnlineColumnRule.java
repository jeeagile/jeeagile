package com.jeeagile.online.entity;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2023-08-21
 * @description 在线表单 数据表字段规则配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileOnlineColumnRule extends AgileBaseTenantModel<AgileOnlineColumnRule> {
    /**
     * 在线表单ID
     */
    @ApiModelProperty(value = "在线表单ID")
    private String formId;
    /**
     * 数据表ID
     */
    @ApiModelProperty(value = "数据表ID")
    private String tableId;
    /**
     * 数据表字段ID
     */
    @ApiModelProperty(value = "数据表字段ID")
    private String columnId;
    /**
     * 规则类型ID
     */
    @ApiModelProperty(value = "规则类型ID")
    private String ruleId;
    /**
     * 规则配置
     */
    @ApiModelProperty(value = "规则配置")
    private String ruleConfig;
}
