package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.mapper.AgileProcessModelMapper;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程模型
 */
@AgileService
public class AgileProcessModelServiceImpl extends AgileBaseServiceImpl<AgileProcessModelMapper, AgileProcessModel> implements IAgileProcessModelService {
    /**
     * 拼装查询条件
     */
    @Override
    public LambdaQueryWrapper<AgileProcessModel> queryWrapper(AgileProcessModel agileProcessModel) {
        LambdaQueryWrapper<AgileProcessModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessModel.class, i -> !"processXml".contains(i.getProperty()));
        if (agileProcessModel != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getModelCode())) {
                lambdaQueryWrapper.eq(AgileProcessModel::getModelCode, agileProcessModel.getModelCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getModelName())) {
                lambdaQueryWrapper.like(AgileProcessModel::getModelName, agileProcessModel.getModelName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getDeploymentStatus())) {
                lambdaQueryWrapper.eq(AgileProcessModel::getDeploymentStatus, agileProcessModel.getDeploymentStatus());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileProcessModel agileProcessModel) {
        agileProcessModel.setDeploymentStatus("2");
        this.validateProcessForm(agileProcessModel);
        this.validateProcessModel(agileProcessModel);
    }

    @Override
    public void updateModelValidate(AgileProcessModel agileProcessModel) {
        agileProcessModel.setDeploymentStatus("2");
        agileProcessModel.setDeploymentTime(null);
        agileProcessModel.setDeploymentId("");
        this.validateProcessForm(agileProcessModel);
        this.validateProcessModel(agileProcessModel);
    }

    /**
     * 校验流程表单配置
     *
     * @param agileProcessModel
     */
    private void validateProcessForm(AgileProcessModel agileProcessModel) {
        if (AgileStringUtil.isEmpty(agileProcessModel.getFormType())) {
            throw new AgileValidateException("请选择表单类型！");
        }
        if (agileProcessModel.getFormType().equals("1")) {
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getFormId())) {
                agileProcessModel.setFormUrl("");
            } else {
                throw new AgileValidateException("请选择流程表单！");
            }
        }

        if (agileProcessModel.getFormType().equals("2")) {
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getFormUrl())) {
                agileProcessModel.setFormId("");
            } else {
                throw new AgileValidateException("请选择填写表单地址！");
            }
        }
    }

    /**
     * 校验流程模型名称和流程模型编码是否存在
     *
     * @param agileProcessModel
     */
    private void validateProcessModel(AgileProcessModel agileProcessModel) {
        LambdaQueryWrapper<AgileProcessModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileProcessModel.getId() != null) {
            lambdaQueryWrapper.ne(AgileProcessModel::getId, agileProcessModel.getId());
        }
        lambdaQueryWrapper.and(queryWrapper ->
                queryWrapper.eq(AgileProcessModel::getModelCode, agileProcessModel.getModelCode()).or().eq(AgileProcessModel::getModelName, agileProcessModel.getModelName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("流程名称或流程编码已存在！");
        }
    }

    @Override
    public AgileProcessModel saveProcessDesigner(String modelId, String processXml) {
        AgileProcessModel agileProcessModel = this.getById(modelId);
        if (agileProcessModel == null || agileProcessModel.isEmptyPk()) {
            throw new AgileValidateException("流程模型已不存在！");
        }
        agileProcessModel.setProcessXml(processXml);
        agileProcessModel.setDeploymentStatus("2");
        agileProcessModel.setUpdateUser(null);
        agileProcessModel.setUpdateTime(null);
        this.updateModel(agileProcessModel);
        return agileProcessModel;
    }
}
