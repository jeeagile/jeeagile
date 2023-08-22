package com.jeeagile.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.online.entity.AgileOnlinePage;
import com.jeeagile.online.mapper.AgileOnlinePageMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

/**
 * @author JeeAgile
 * @date 2023-07-31
 * @description 在线表单 表单页面 接口实现
 */
@AgileService
public class AgileOnlinePageServiceImpl extends AgileBaseServiceImpl<AgileOnlinePageMapper, AgileOnlinePage> implements IAgileOnlinePageService {

    @Override
    public LambdaQueryWrapper<AgileOnlinePage> queryWrapper(AgileOnlinePage agileOnlinePage) {
        if (agileOnlinePage == null || AgileStringUtil.isEmpty(agileOnlinePage.getFormId())) {
            throw new AgileValidateException("表单ID不能空！");
        }
        LambdaQueryWrapper<AgileOnlinePage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileOnlinePage.class, i -> !"widgetJson".contains(i.getProperty()) || !"paramJson".contains(i.getProperty()));
        lambdaQueryWrapper.eq(AgileOnlinePage::getFormId, agileOnlinePage.getFormId());
        if (AgileStringUtil.isNotEmpty(agileOnlinePage.getPageCode())) {
            lambdaQueryWrapper.eq(AgileOnlinePage::getPageCode, agileOnlinePage.getPageCode());
        }
        if (AgileStringUtil.isNotEmpty(agileOnlinePage.getPageName())) {
            lambdaQueryWrapper.like(AgileOnlinePage::getPageName, agileOnlinePage.getPageName());
        }
        if (AgileStringUtil.isNotEmpty(agileOnlinePage.getPageType())) {
            lambdaQueryWrapper.like(AgileOnlinePage::getPageType, agileOnlinePage.getPageType());
        }
        lambdaQueryWrapper.orderByAsc(AgileOnlinePage::getPageType);
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileOnlinePage agileOnlinePage) {
        this.validateData(agileOnlinePage);
    }

    @Override
    public void updateModelValidate(AgileOnlinePage agileOnlinePage) {
        AgileOnlinePage oldOnlinePage = this.getById(agileOnlinePage.getId());
        if (!oldOnlinePage.getTableId().equals(agileOnlinePage.getTableId())) {
            agileOnlinePage.setWidgetJson(oldOnlinePage.getWidgetJson());
            agileOnlinePage.setParamJson(oldOnlinePage.getParamJson());
        }
        this.validateData(agileOnlinePage);
    }

    /**
     * 校验表单页面编码和表单页面名称
     */
    private void validateData(AgileOnlinePage agileOnlinePage) {
        LambdaQueryWrapper<AgileOnlinePage> queryWrapper = new LambdaQueryWrapper<>();
        if (agileOnlinePage.getId() != null) {
            queryWrapper.ne(AgileOnlinePage::getId, agileOnlinePage.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileOnlinePage::getPageCode, agileOnlinePage.getPageCode()).or().eq(AgileOnlinePage::getPageName, agileOnlinePage.getPageName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("表单页面名称或表单页面编码已存在！");
        }
    }

    @Override
    public boolean designer(AgileOnlinePage agileOnlinePage) {
        LambdaUpdateWrapper<AgileOnlinePage> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileOnlinePage::getWidgetJson, agileOnlinePage.getWidgetJson());
        lambdaUpdateWrapper.set(AgileOnlinePage::getParamJson, agileOnlinePage.getParamJson());
        lambdaUpdateWrapper.eq(AgileOnlinePage::getId, agileOnlinePage.getId());
        return this.update(lambdaUpdateWrapper);
    }
}
