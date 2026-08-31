package com.jeeagile.drools.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则数据对象映射 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileDroolsRuleModel extends AgileBaseModel<AgileDroolsRuleModel> {
    /**
     * 规则主键id
     */
    @NotNull(message = "规则主键id不能为空！")
    private String ruleId;

    /**
     * 对象主键id
     */
    @NotNull(message = "对象主键id不能为空！")
    private String modelId;

}
