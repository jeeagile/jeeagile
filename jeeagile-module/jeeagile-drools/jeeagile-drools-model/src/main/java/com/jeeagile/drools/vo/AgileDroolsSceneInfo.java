package com.jeeagile.drools.vo;

import com.jeeagile.drools.entity.AgileDroolsRule;
import com.jeeagile.drools.entity.AgileDroolsScene;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgileDroolsSceneInfo extends AgileDroolsScene {
    /**
     * 场景规则列表
     */
    private List<AgileDroolsRule> droolsRuleList = new ArrayList<>();
    /**
     * 数据对象列表
     */
    private List<AgileDroolsModelInfo> droolsModelList = new ArrayList<>();
}
