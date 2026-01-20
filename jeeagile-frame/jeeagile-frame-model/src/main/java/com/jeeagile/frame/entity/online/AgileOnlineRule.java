package com.jeeagile.frame.entity.online;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2023-08-21
 * @description 在线表单 规则类型
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileOnlineRule extends AgileBaseTenantModel<AgileOnlineRule> {
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    @NotBlank(message = "数据验证失败，规则名称不能为空！")
    private String ruleName;

    /**
     * 规则类型
     */
    @ApiModelProperty(value = "规则类型")
    @NotNull(message = "数据验证失败，规则类型不能为空！")
    private String ruleType;

    /**
     * 自定义规则正则表达式
     */
    @ApiModelProperty(value = "自定义规则正则表达式")
    private String pattern;
}
