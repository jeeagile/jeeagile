package com.jeeagile.drools.service;

import com.jeeagile.drools.entity.AgileDroolsRuleModel;
import com.jeeagile.drools.entity.AgileDroolsSceneRule;
import com.jeeagile.frame.service.IAgileBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 场景规则关联 Service接口
 */
public interface IAgileDroolsSceneRuleService extends IAgileBaseService<AgileDroolsSceneRule>  {
    /**
     * 根据规则id查询规则数据对象映射列表
     */
    List<String> getSceneRuleIdBySceneId(Serializable sceneId);

    /**
     * 获取规则对象列表
     *
     * @param sceneId
     * @return
     */
    List<AgileDroolsSceneRule> selectSceneModelListByRuleId(String sceneId);
}
