package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysPost;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessForm;
import com.jeeagile.process.mapper.AgileProcessFormMapper;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程表单
 */
@AgileService
public class AgileProcessFormServiceImpl extends AgileBaseServiceImpl<AgileProcessFormMapper, AgileProcessForm> implements IAgileProcessFormService {
    @Override
    public LambdaQueryWrapper<AgileProcessForm> queryWrapper(AgileProcessForm agileProcessForm) {
        LambdaQueryWrapper<AgileProcessForm> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessForm.class, i -> !"formFields".contains(i.getProperty()) || !"formConfig".contains(i.getProperty()));
        if (agileProcessForm != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessForm.getFormCode())) {
                lambdaQueryWrapper.eq(AgileProcessForm::getFormCode, agileProcessForm.getFormCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessForm.getFormName())) {
                lambdaQueryWrapper.like(AgileProcessForm::getFormName, agileProcessForm.getFormName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessForm.getFormStatus())) {
                lambdaQueryWrapper.eq(AgileProcessForm::getFormStatus, agileProcessForm.getFormStatus());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileProcessForm::getFormCode);
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileProcessForm agileProcessForm) {
        this.validateProcessForm(agileProcessForm);
    }

    @Override
    public void updateModelValidate(AgileProcessForm agileProcessForm) {
        this.validateProcessForm(agileProcessForm);
    }

    /**
     * 校验岗位编码、岗位名称是否存在
     */
    private void validateProcessForm(AgileProcessForm agileProcessForm) {
        LambdaQueryWrapper<AgileProcessForm> queryWrapper = new LambdaQueryWrapper<>();
        if (agileProcessForm.getId() != null) {
            queryWrapper.ne(AgileProcessForm::getId, agileProcessForm.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileProcessForm::getFormCode, agileProcessForm.getFormCode()).or().eq(AgileProcessForm::getFormName, agileProcessForm.getFormName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("流程表单编码或表单名称已存在！");
        }
    }

    @Override
    public AgileProcessForm saveProcessDesigner(String formId, String formConf, String formFields) {
        AgileProcessForm agileProcessForm = this.getById(formId);
        if (agileProcessForm == null || agileProcessForm.isEmptyPk()) {
            throw new AgileValidateException("流程表单已不存在！");
        }
        agileProcessForm.setFormConf(formConf);
        agileProcessForm.setFormFields(formFields);
        agileProcessForm.setUpdateUser(null);
        agileProcessForm.setUpdateTime(null);
        this.updateModel(agileProcessForm);
        return agileProcessForm;
    }
}
