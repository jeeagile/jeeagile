package com.jeeagile.drools.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 场景规则关联 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileDroolsSceneRule extends AgileBaseModel<AgileDroolsSceneRule> {
    /**
     * 场景主键id
     */
    @NotNull(message = "场景主键id不能为空！")
    private String sceneId;

    /**
     * 规则主键ID
     */
    @NotNull(message = "规则主键ID不能为空！")
    private String ruleId;

}
