package com.jeeagile.drools.service;

import com.jeeagile.drools.entity.AgileDroolsRule;
import com.jeeagile.drools.vo.AgileDroolsRuleInfo;
import com.jeeagile.frame.service.IAgileBaseService;

import java.io.Serializable;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则配置 Service接口
 */
public interface IAgileDroolsRuleService extends IAgileBaseService<AgileDroolsRule> {
    /**
     * 修改规则配置状态
     */
    boolean changeStatus(Serializable ruleId, String ruleStatus);

    /**
     * 或者规则执行信息
     */
    AgileDroolsRuleInfo info(Serializable ruleId);

    /**
     * 保存规则内容
     */
    boolean saveRuleContent(AgileDroolsRule agileDroolsRule);

    /**
     * 验证规则内容
     */
    boolean validateRuleContent(String ruleId, String ruleContent);

    /**
     * 规则测试
     */
    Object test(String ruleId, String ruleContent, Map paramData);
}
