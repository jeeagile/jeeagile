package com.jeeagile.drools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.entity.AgileDroolsRuleModel;
import com.jeeagile.drools.mapper.AgileDroolsRuleModelMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则数据对象映射 业务处理层
 */
@AgileService
public class AgileDroolsRuleModelServiceImpl extends AgileBaseServiceImpl<AgileDroolsRuleModelMapper, AgileDroolsRuleModel> implements IAgileDroolsRuleModelService {
    /**
     * 拼装查询条件
     */
    @Override
    public LambdaQueryWrapper<AgileDroolsRuleModel> queryWrapper(AgileDroolsRuleModel agileDroolsRuleModel) {
        LambdaQueryWrapper<AgileDroolsRuleModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsRuleModel != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsRuleModel.getRuleId())) {
                lambdaQueryWrapper.eq(AgileDroolsRuleModel::getRuleId, agileDroolsRuleModel.getRuleId());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRuleModel.getModelId())) {
                lambdaQueryWrapper.eq(AgileDroolsRuleModel::getModelId, agileDroolsRuleModel.getModelId());
            }
        }
        return lambdaQueryWrapper;
    }
    @Override
    public List<String> getRuleModelIdByRuleId(Serializable ruleId) {
        List<String> modelIdList = new ArrayList<>();
        LambdaQueryWrapper<AgileDroolsRuleModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsRuleModel::getRuleId, ruleId);
        List<AgileDroolsRuleModel> agileDroolsRuleModelList = this.list(lambdaQueryWrapper);
        agileDroolsRuleModelList.forEach(item -> modelIdList.add(item.getModelId()));
        return modelIdList;
    }

    @Override
    public List<AgileDroolsRuleModel> selectRuleModelListByRuleId(String ruleId) {
        LambdaQueryWrapper<AgileDroolsRuleModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsRuleModel::getRuleId, ruleId);
        return this.list(lambdaQueryWrapper);
    }


}
