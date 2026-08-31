package com.jeeagile.drools.vo;

import com.jeeagile.drools.entity.AgileDroolsRuleLogger;
import com.jeeagile.drools.entity.AgileDroolsSceneLogger;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgileDroolsLoggerInfo extends AgileDroolsSceneLogger {
    private List<AgileDroolsRuleLogger> droolsRuleLoggerList;
}
