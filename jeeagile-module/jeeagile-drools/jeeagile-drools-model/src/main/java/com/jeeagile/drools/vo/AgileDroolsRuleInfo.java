package com.jeeagile.drools.vo;

import com.jeeagile.drools.entity.AgileDroolsRule;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则配置 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AgileDroolsRuleInfo extends AgileDroolsRule {
    /**
     * 数据对象列表
     */
    private List<AgileDroolsModelInfo> droolsModelList = new ArrayList<>();
}
