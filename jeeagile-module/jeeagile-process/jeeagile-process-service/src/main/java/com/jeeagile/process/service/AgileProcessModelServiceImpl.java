package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.constants.online.OnlinePageType;
import com.jeeagile.frame.entity.online.AgileOnlinePage;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.online.IAgileOnlinePageService;
import com.jeeagile.frame.util.AgileBeanUtils;
import com.jeeagile.process.constants.ProcessFormType;
import com.jeeagile.process.constants.ProcessDeploymentStatus;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessForm;
import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.mapper.AgileProcessModelMapper;
import com.jeeagile.process.support.IAgileProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程模型
 */
@AgileService
public class AgileProcessModelServiceImpl extends AgileBaseServiceImpl<AgileProcessModelMapper, AgileProcessModel> implements IAgileProcessModelService {
    @Autowired
    private IAgileProcessService agileProcessService;
    @Autowired
    private IAgileProcessFormService agileProcessFormService;
    @Autowired
    private IAgileProcessDefinitionService agileProcessDefinitionService;
    @Autowired
    private IAgileOnlinePageService agileOnlinePageService;

    @Override
    public LambdaQueryWrapper<AgileProcessModel> queryWrapper(AgileProcessModel agileProcessModel) {
        LambdaQueryWrapper<AgileProcessModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessModel.class, i -> !"modelXml".contains(i.getProperty()));
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
        handlerDeploymentStatus(agileProcessModel);
        this.validateProcessForm(agileProcessModel);
        this.validateProcessModel(agileProcessModel);
    }

    @Override
    public void updateModelValidate(AgileProcessModel agileProcessModel) {
        handlerDeploymentStatus(agileProcessModel);
        agileProcessModel.setDeploymentTime(null);
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
    public AgileProcessModel saveProcessDesigner(String modelId, String modelXml) {
        AgileProcessModel agileProcessModel = this.getById(modelId);
        if (agileProcessModel == null || agileProcessModel.isEmptyPk()) {
            throw new AgileValidateException("流程模型已不存在！");
        }
        agileProcessModel.setModelXml(modelXml);
        handlerDeploymentStatus(agileProcessModel);
        this.updateById(agileProcessModel);
        return agileProcessModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processDeployment(String modelId) {
        AgileProcessModel agileProcessModel = this.getById(modelId);
        if (agileProcessModel != null && agileProcessModel.isNotEmptyPk()) {
            if (ProcessDeploymentStatus.PUBLISHED.equals(agileProcessModel.getDeploymentStatus())) {
                throw new AgileFrameException("当前流程已处于发布状态！");
            }
            if (AgileStringUtil.isEmpty(agileProcessModel.getModelXml())) {
                throw new AgileFrameException("请先进行流程设计！");
            }
            AgileProcessForm agileProcessForm = null;
            if (agileProcessModel.getFormType().equals("1")) {
                agileProcessForm = agileProcessFormService.getById(agileProcessModel.getFormId());
                if (agileProcessForm == null || agileProcessForm.isEmptyPk()) {
                    throw new AgileFrameException("流程表单不存在，请核实！");
                }
                if ("1".equals(agileProcessForm.getFormStatus())) {
                    throw new AgileFrameException("流程表单已停用！");
                }
            }
            //流程发布
            String deploymentId = agileProcessService.processDeployment(agileProcessModel);
            String definitionId = agileProcessService.getProcessDefinitionId(deploymentId);
            agileProcessModel.setDeploymentStatus(ProcessDeploymentStatus.PUBLISHED);
            agileProcessModel.setDeploymentTime(new Date());
            AgileProcessDefinition agileProcessDefinition = new AgileProcessDefinition();
            AgileBeanUtils.copyProperties(agileProcessModel, agileProcessDefinition);
            if (agileProcessModel.getFormType().equals("1")) {
                agileProcessDefinition.setFormName(agileProcessForm.getFormName());
                agileProcessDefinition.setFormConf(agileProcessForm.getFormConf());
                agileProcessDefinition.setFormFields(agileProcessForm.getFormFields());
            }
            agileProcessDefinition.setSuspensionState(1);
            agileProcessDefinition.setId(definitionId);
            agileProcessDefinition.setModelId(agileProcessModel.getId());
            agileProcessDefinitionService.saveModel(agileProcessDefinition);

            return this.updateById(agileProcessModel);
        } else {
            throw new AgileFrameException("流程已不存在无法进行发布操作！");
        }
    }


    /**
     * 处理发布状态 如果已处于发布状态则修改状态为未发布，且将版本号加一
     */
    private synchronized void handlerDeploymentStatus(AgileProcessModel agileProcessModel) {
        String deploymentStatus = agileProcessModel.getDeploymentStatus();
        if (AgileStringUtil.isNotEmpty(deploymentStatus) && deploymentStatus.equals(ProcessDeploymentStatus.PUBLISHED)) {
            agileProcessModel.setDeploymentStatus(ProcessDeploymentStatus.UNPUBLISHED);
            agileProcessModel.setDeploymentTime(null);
            agileProcessModel.setModelVersion(agileProcessModel.getModelVersion() + 1);
        } else {
            agileProcessModel.setDeploymentStatus(ProcessDeploymentStatus.UNPUBLISHED);
            agileProcessModel.setDeploymentTime(null);
        }
    }

    @Override
    public Map<String, Object> selectProcessOnlinePageList() {
        Map rtnMap = new HashMap();
        LambdaQueryWrapper<AgileProcessModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessModel::getId, AgileProcessModel::getFormId, AgileProcessModel::getModelName);
        lambdaQueryWrapper.eq(AgileProcessModel::getDeploymentStatus, ProcessDeploymentStatus.PUBLISHED);
        lambdaQueryWrapper.eq(AgileProcessModel::getFormType, ProcessFormType.ONLINE_FORM);
        List<AgileProcessModel> agileProcessModelList = this.list(lambdaQueryWrapper);
        List<AgileOnlinePage> agileOnlinePageList = new ArrayList<>();
        agileProcessModelList.forEach(agileProcessModel -> {
            LambdaQueryWrapper<AgileOnlinePage> pageQueryWrapper = new LambdaQueryWrapper<>();
            pageQueryWrapper.select(AgileOnlinePage::getId, AgileOnlinePage::getFormId, AgileOnlinePage::getPageName, AgileOnlinePage::getPageType);
            pageQueryWrapper.eq(AgileOnlinePage::getFormId, agileProcessModel.getFormId());
            pageQueryWrapper.in(AgileOnlinePage::getPageType, OnlinePageType.ORDER);
            agileOnlinePageList.addAll(this.agileOnlinePageService.list(pageQueryWrapper));
        });
        rtnMap.put("processList", agileProcessModelList);
        rtnMap.put("orderPageList", agileOnlinePageList);
        return rtnMap;
    }
}
