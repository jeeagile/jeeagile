package com.jeeagile.drools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.entity.AgileDroolsModelField;
import com.jeeagile.drools.mapper.AgileDroolsModelFieldMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-13 15:26:20
 * @description 规则引擎 数据对象字段 业务处理层
 */
@AgileService
public class AgileDroolsModelFieldServiceImpl extends AgileBaseServiceImpl<AgileDroolsModelFieldMapper, AgileDroolsModelField>  implements IAgileDroolsModelFieldService {
    /**
    * 拼装查询条件
    */
    @Override
    public LambdaQueryWrapper<AgileDroolsModelField> queryWrapper(AgileDroolsModelField agileDroolsModelField) {
        LambdaQueryWrapper<AgileDroolsModelField> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsModelField != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getModelId())) {
                lambdaQueryWrapper.eq(AgileDroolsModelField::getModelId, agileDroolsModelField.getModelId());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getFieldName())) {
                lambdaQueryWrapper.like(AgileDroolsModelField::getFieldName, agileDroolsModelField.getFieldName());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getFieldLabel())) {
                lambdaQueryWrapper.eq(AgileDroolsModelField::getFieldLabel, agileDroolsModelField.getFieldLabel());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getFieldType())) {
                lambdaQueryWrapper.eq(AgileDroolsModelField::getFieldType, agileDroolsModelField.getFieldType());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getObjectId())) {
                lambdaQueryWrapper.eq(AgileDroolsModelField::getObjectId, agileDroolsModelField.getObjectId());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getFieldDesc())) {
                lambdaQueryWrapper.eq(AgileDroolsModelField::getFieldDesc, agileDroolsModelField.getFieldDesc());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getListFlag())) {
                lambdaQueryWrapper.eq(AgileDroolsModelField::getListFlag, agileDroolsModelField.getListFlag());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModelField.getInputFlag())) {
                lambdaQueryWrapper.eq(AgileDroolsModelField::getInputFlag, agileDroolsModelField.getInputFlag());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileDroolsModelField> selectModelFieldList(String modelId) {
        LambdaQueryWrapper<AgileDroolsModelField> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsModelField::getModelId, modelId);
        lambdaQueryWrapper.orderByAsc(AgileDroolsModelField::getFieldSort);
        return this.list(lambdaQueryWrapper);
    }
}
