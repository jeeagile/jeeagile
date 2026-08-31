package com.jeeagile.drools.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则配置 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileDroolsRule extends AgileBaseModel<AgileDroolsRule> {
    /**
     * 规则编码
     */
    @NotNull(message = "规则编码不能为空！")
    private String ruleCode;

    /**
     * 规则名称
     */
    @NotNull(message = "规则名称不能为空！")
    private String ruleName;

    /**
     * 规则类型
     */
    @NotNull(message = "规则类型不能为空！")
    private String ruleType;

    /**
     * 规则包名
     */
    @NotNull(message = "规则包名不能为空！")
    private String rulePackage;

    /**
     * 规则状态
     */
    private String ruleStatus;

    /**
     * 规则内容
     */
    private String ruleContent;

    /**
     * 规则描述
     */
    private String ruleDesc;

    /**
     * 数据对象
     */
    @TableField(exist = false)
    private List<String> modelIdList = new ArrayList<>();
}
