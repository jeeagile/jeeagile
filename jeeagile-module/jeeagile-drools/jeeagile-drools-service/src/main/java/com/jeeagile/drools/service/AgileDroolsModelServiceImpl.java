package com.jeeagile.drools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.constants.AgileSwitchStatus;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.constants.DroolsFieldType;
import com.jeeagile.drools.entity.AgileDroolsModel;
import com.jeeagile.drools.entity.AgileDroolsModelField;
import com.jeeagile.drools.kie.AgileKieRule;
import com.jeeagile.drools.kie.AgileKieTemplate;
import com.jeeagile.drools.mapper.AgileDroolsModelMapper;
import com.jeeagile.drools.util.AgileDroolsUtil;
import com.jeeagile.drools.vo.AgileDroolsModelFieldInfo;
import com.jeeagile.drools.vo.AgileDroolsModelInfo;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-12 10:58:02
 * @description 规则引擎 数据对象 业务处理层
 */
@AgileService
public class AgileDroolsModelServiceImpl extends AgileBaseServiceImpl<AgileDroolsModelMapper, AgileDroolsModel> implements IAgileDroolsModelService {
    @Autowired
    private IAgileDroolsModelFieldService agileDroolsModelFieldService;
    @Autowired
    private AgileKieTemplate agileKieTemplate;

    /**
     * 拼装查询条件
     */
    @Override
    public LambdaQueryWrapper<AgileDroolsModel> queryWrapper(AgileDroolsModel agileDroolsModel) {
        LambdaQueryWrapper<AgileDroolsModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsModel != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getModelName())) {
                lambdaQueryWrapper.like(AgileDroolsModel::getModelName, agileDroolsModel.getModelName());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getModelLabel())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getModelLabel, agileDroolsModel.getModelLabel());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getModelType())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getModelType, agileDroolsModel.getModelType());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getModelPackage())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getModelPackage, agileDroolsModel.getModelPackage());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getSuperModel())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getSuperModel, agileDroolsModel.getSuperModel());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getModelStatus())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getModelStatus, agileDroolsModel.getModelStatus());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getInputFlag())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getInputFlag, agileDroolsModel.getInputFlag());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getOutputFlag())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getOutputFlag, agileDroolsModel.getOutputFlag());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsModel.getModelDesc())) {
                lambdaQueryWrapper.eq(AgileDroolsModel::getModelDesc, agileDroolsModel.getModelDesc());
            }
        }
        return lambdaQueryWrapper;
    }

    /**
     * 校验数据对象
     */
    @Override
    public void validateModel(AgileDroolsModel agileDroolsModel) throws AgileValidateException {
        LambdaQueryWrapper<AgileDroolsModel> queryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsModel.getId() != null) {
            queryWrapper.ne(AgileDroolsModel::getId, agileDroolsModel.getId());
        }
        queryWrapper.eq(AgileDroolsModel::getModelPackage, agileDroolsModel.getModelPackage());
        queryWrapper.eq(AgileDroolsModel::getModelName, agileDroolsModel.getModelName());
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("对象已存在请核实对象名称和对象包名！");
        }
    }

    @Override
    public boolean changeStatus(Serializable modelId, String modelStatus) {
        if (!AgileSwitchStatus.isValid(modelStatus)) {
            throw new AgileValidateException("状态值非法！");
        }
        AgileDroolsModel agileDroolsModel = this.getById(modelId);
        if (agileDroolsModel == null || agileDroolsModel.isEmptyPk()) {
            throw new AgileValidateException("对象不存在！");
        }
        if (AgileSwitchStatus.ENABLE.equals(modelStatus)) {
            List<AgileDroolsModelField> droolsModelFieldList = agileDroolsModelFieldService.selectModelFieldList((String) modelId);
            if (AgileStringUtil.isEmpty(droolsModelFieldList)) {
                throw new AgileValidateException("该对象未设置字段不能启用！");
            }
            String sourceCode = AgileDroolsUtil.getModelSourceCode(agileDroolsModel, this.selectModelFieldInfoList(agileDroolsModel.getId()));
            AgileKieRule agileKieRule = new AgileKieRule();
            agileKieRule.setContent(sourceCode);
            agileKieRule.setPath(agileDroolsModel.getId() + ".drl");
            agileKieTemplate.addOrUpdateKieRule(agileKieRule);
        } else {
            agileKieTemplate.removeContent(agileDroolsModel.getId() + ".drl");
        }
        LambdaUpdateWrapper<AgileDroolsModel> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileDroolsModel::getModelStatus, modelStatus);
        lambdaUpdateWrapper.eq(AgileDroolsModel::getId, modelId);
        return this.update(lambdaUpdateWrapper);

    }

    @Override
    public List<AgileDroolsModelFieldInfo> selectModelFieldInfoList(String modelId) {
        List<AgileDroolsModelField> droolsModelFieldList = agileDroolsModelFieldService.selectModelFieldList(modelId);
        List<AgileDroolsModelFieldInfo> droolsModelFieldInfoList = new ArrayList<>();
        for (AgileDroolsModelField agileDroolsModelField : droolsModelFieldList) {
            AgileDroolsModelFieldInfo droolsModelFieldInfo = new AgileDroolsModelFieldInfo();
            BeanUtils.copyProperties(agileDroolsModelField, droolsModelFieldInfo);
            if (DroolsFieldType.Object.equals(agileDroolsModelField.getFieldType())) {
                AgileDroolsModel droolsModel = this.getById(agileDroolsModelField.getObjectId());
                if (droolsModel == null || droolsModel.isEmptyPk()) {
                    continue;
                }
                AgileDroolsModelInfo droolsModelInfo = new AgileDroolsModelInfo();
                BeanUtils.copyProperties(droolsModel, droolsModelInfo);
                droolsModelInfo.setDroolsModelFieldList(this.selectModelFieldInfoList(droolsModelInfo.getId()));
                droolsModelFieldInfo.setDroolsModelInfo(droolsModelInfo);
            }
            droolsModelFieldInfoList.add(droolsModelFieldInfo);
        }
        return droolsModelFieldInfoList;
    }


}
