package com.jeeagile.drools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.entity.AgileDroolsRuleModel;
import com.jeeagile.drools.entity.AgileDroolsSceneRule;
import com.jeeagile.drools.mapper.AgileDroolsSceneRuleMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 场景规则关联 业务处理层
 */
@AgileService
public class AgileDroolsSceneRuleServiceImpl extends AgileBaseServiceImpl<AgileDroolsSceneRuleMapper, AgileDroolsSceneRule>  implements IAgileDroolsSceneRuleService {
    /**
    * 拼装查询条件
    */
    @Override
    public LambdaQueryWrapper<AgileDroolsSceneRule> queryWrapper(AgileDroolsSceneRule agileDroolsSceneRule) {
        LambdaQueryWrapper<AgileDroolsSceneRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsSceneRule != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneRule.getSceneId())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneRule::getSceneId, agileDroolsSceneRule.getSceneId());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsSceneRule.getRuleId())) {
                lambdaQueryWrapper.eq(AgileDroolsSceneRule::getRuleId, agileDroolsSceneRule.getRuleId());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public List<String> getSceneRuleIdBySceneId(Serializable sceneId) {
        List<String> ruleIdList = new ArrayList<>();
        LambdaQueryWrapper<AgileDroolsSceneRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsSceneRule::getRuleId, sceneId);
        List<AgileDroolsSceneRule> agileDroolsSceneRuleList = this.list(lambdaQueryWrapper);
        agileDroolsSceneRuleList.forEach(item -> ruleIdList.add(item.getRuleId()));
        return ruleIdList;
    }

    @Override
    public List<AgileDroolsSceneRule> selectSceneModelListByRuleId(String sceneId) {
        LambdaQueryWrapper<AgileDroolsSceneRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsSceneRule::getRuleId, sceneId);
        return this.list(lambdaQueryWrapper);
    }
}
