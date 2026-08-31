package com.jeeagile.drools.service;

import com.jeeagile.drools.entity.AgileDroolsRuleModel;
import com.jeeagile.frame.service.IAgileBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则数据对象映射 Service接口
 */
public interface IAgileDroolsRuleModelService extends IAgileBaseService<AgileDroolsRuleModel> {
    /**
     * 根据规则id查询规则数据对象映射列表
     */
    List<String> getRuleModelIdByRuleId(Serializable ruleId);

    /**
     * 获取规则对象列表
     *
     * @param ruleId
     * @return
     */
    List<AgileDroolsRuleModel> selectRuleModelListByRuleId(String ruleId);
}
