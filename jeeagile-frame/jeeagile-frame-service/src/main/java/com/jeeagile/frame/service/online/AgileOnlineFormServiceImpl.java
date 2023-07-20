package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.online.AgileOnlineForm;
import com.jeeagile.frame.mapper.online.AgileOnlineFormMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

/**
 * @author JeeAgile
 * @date 2021-07-19
 * @description 在线表单 表单管理 接口实现
 */
@AgileService
public class AgileOnlineFormServiceImpl extends AgileBaseServiceImpl<AgileOnlineFormMapper, AgileOnlineForm> implements IAgileOnlineFormService {

    @Override
    public LambdaQueryWrapper<AgileOnlineForm> queryWrapper(AgileOnlineForm agileOnlineForm) {
        LambdaQueryWrapper<AgileOnlineForm> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileOnlineForm != null) {
            if (AgileStringUtil.isNotEmpty(agileOnlineForm.getFormCode())) {
                lambdaQueryWrapper.eq(AgileOnlineForm::getFormCode, agileOnlineForm.getFormCode());
            }
            if (AgileStringUtil.isNotEmpty(agileOnlineForm.getFormName())) {
                lambdaQueryWrapper.like(AgileOnlineForm::getFormName, agileOnlineForm.getFormName());
            }
            if (AgileStringUtil.isNotEmpty(agileOnlineForm.getFormType())) {
                lambdaQueryWrapper.eq(AgileOnlineForm::getFormType, agileOnlineForm.getFormType());
            }
            if (AgileStringUtil.isNotEmpty(agileOnlineForm.getPublishStatus())) {
                lambdaQueryWrapper.eq(AgileOnlineForm::getPublishStatus, agileOnlineForm.getPublishStatus());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileOnlineForm agileOnlineForm) {
        this.validateData(agileOnlineForm);
    }

    @Override
    public void updateModelValidate(AgileOnlineForm agileOnlineForm) {
        this.validateData(agileOnlineForm);
    }

    /**
     * 校验表单编码和表单名称
     */
    private void validateData(AgileOnlineForm agileOnlineForm) {
        LambdaQueryWrapper<AgileOnlineForm> queryWrapper = new LambdaQueryWrapper<>();
        if (agileOnlineForm.getId() != null) {
            queryWrapper.ne(AgileOnlineForm::getId, agileOnlineForm.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileOnlineForm::getFormCode, agileOnlineForm.getFormCode()).or().eq(AgileOnlineForm::getFormName, agileOnlineForm.getFormName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("表单名称或表单编码已存在！");
        }
    }
}
