package com.jeeagile.online.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineColumnRule {
    /**
     * 字段规则配置ID
     */
    @ApiModelProperty(value = "字段规则配置ID")
    private String id;
    /**
     * 数据表字段ID
     */
    @ApiModelProperty(value = "数据表字段ID")
    private String columnId;
    /**
     * 规则配置
     */
    @ApiModelProperty(value = "规则配置")
    private String ruleConfig;
    /**
     * 规则类型ID
     */
    @ApiModelProperty(value = "规则类型ID")
    private String ruleId;
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    /**
     * 规则类型
     */
    @ApiModelProperty(value = "规则类型")
    private String ruleType;
    /**
     * 自定义规则正则表达式
     */
    @ApiModelProperty(value = "自定义规则正则表达式")
    private String pattern;
}
