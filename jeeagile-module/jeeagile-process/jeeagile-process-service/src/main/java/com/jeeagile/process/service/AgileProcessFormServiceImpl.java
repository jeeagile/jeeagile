package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
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
}
