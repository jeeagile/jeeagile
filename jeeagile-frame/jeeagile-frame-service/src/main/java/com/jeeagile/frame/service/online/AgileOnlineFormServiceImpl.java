package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.online.*;
import com.jeeagile.frame.constants.online.OnlineFormStatus;
import com.jeeagile.frame.constants.online.OnlinePageType;
import com.jeeagile.core.constants.AgilePublishStatus;
import com.jeeagile.frame.mapper.online.AgileOnlineFormMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.vo.online.OnlinePageRender;
import com.jeeagile.frame.vo.online.OnlinePageTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-07-19
 * @description 在线表单 表单管理 接口实现
 */
@AgileService
public class AgileOnlineFormServiceImpl extends AgileBaseServiceImpl<AgileOnlineFormMapper, AgileOnlineForm> implements IAgileOnlineFormService {

    @Autowired
    private IAgileOnlineTableService agileOnlineTableService;
    @Autowired
    private IAgileOnlineColumnService agileOnlineColumnService;
    @Autowired
    private IAgileOnlineColumnRuleService agileOnlineColumnRuleService;
    @Autowired
    private IAgileOnlinePageService agileOnlinePageService;


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
        agileOnlineForm.setFormStatus(OnlineFormStatus.DATA_MODEL);
        agileOnlineForm.setPublishStatus(AgilePublishStatus.UNPUBLISHED);
        this.validateData(agileOnlineForm);
    }

    @Override
    public void updateModelValidate(AgileOnlineForm agileOnlineForm) {
        // 防止对表单状态和发布状态值进行修改
        agileOnlineForm.setFormStatus(null);
        agileOnlineForm.setPublishStatus(null);
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

    @Override
    public boolean publish(String id, String publishStatus) {
        if (!AgilePublishStatus.isValid(publishStatus)) {
            throw new AgileValidateException("非法发布状态值！");
        }
        AgileOnlineForm agileOnlineForm = this.getById(id);
        if (agileOnlineForm == null || agileOnlineForm.isEmptyPk()) {
            throw new AgileValidateException("表单已不存在！");
        }
        if (AgilePublishStatus.PUBLISHED.equals(publishStatus) && !OnlineFormStatus.PAGE_DESIGN.equals(agileOnlineForm.getFormStatus())) {
            throw new AgileValidateException("表单还处于" + OnlineFormStatus.getDesc(agileOnlineForm.getFormStatus()) + "不能进行发布！");
        }
        LambdaUpdateWrapper<AgileOnlineForm> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileOnlineForm::getPublishStatus, publishStatus);
        lambdaUpdateWrapper.eq(AgileOnlineForm::getId, id);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean changeStatus(String id, String formStatus) {
        if (!OnlineFormStatus.isValid(formStatus)) {
            throw new AgileValidateException("非法表单状态值！");
        }
        LambdaUpdateWrapper<AgileOnlineForm> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileOnlineForm::getFormStatus, formStatus);
        lambdaUpdateWrapper.eq(AgileOnlineForm::getId, id);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public OnlinePageRender render(String pageId) {
        AgileOnlinePage agileOnlinePage = this.agileOnlinePageService.getById(pageId);
        if (agileOnlinePage == null || agileOnlinePage.isEmptyPk()) {
            throw new AgileValidateException("表单页面已不存在");
        }
        AgileOnlineForm agileOnlineForm = this.getById(agileOnlinePage.getFormId());
        if (agileOnlineForm == null || agileOnlineForm.isEmptyPk()) {
            throw new AgileValidateException("表单已不存在");
        }
        List<OnlinePageTable> pageTableList = this.agileOnlineTableService.pageTableList(pageId);
        OnlinePageRender onlinePageRender = new OnlinePageRender();
        onlinePageRender.setOnlineForm(agileOnlineForm);
        onlinePageRender.setOnlinePage(agileOnlinePage);
        onlinePageRender.setPageTableList(pageTableList);
        return onlinePageRender;
    }

    @Override
    public Map selectFormPageList() {
        Map rtnMap = new HashMap();
        LambdaQueryWrapper<AgileOnlineForm> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineForm::getPublishStatus, AgilePublishStatus.PUBLISHED);
        List<AgileOnlineForm> agileOnlineFormList = this.list(lambdaQueryWrapper);
        List<AgileOnlinePage> agileOnlinePageList = new ArrayList<>();
        agileOnlineFormList.forEach(agileOnlineForm -> {
            LambdaQueryWrapper<AgileOnlinePage> pageQueryWrapper = new LambdaQueryWrapper<>();
            pageQueryWrapper.eq(AgileOnlinePage::getFormId, agileOnlineForm.getId());
            pageQueryWrapper.in(AgileOnlinePage::getPageType, OnlinePageType.QUERY, OnlinePageType.ORDER);
            agileOnlinePageList.addAll(this.agileOnlinePageService.list(pageQueryWrapper));
        });
        rtnMap.put("onlineFormList", agileOnlineFormList);
        rtnMap.put("onlinePageList", agileOnlinePageList);
        return rtnMap;
    }

    @Override
    public boolean deleteModel(Serializable id) {
        this.deleteOnlineTable(id);
        this.deleteOnlineColumn(id);
        this.deleteOnlineColumnRule(id);
        this.deleteOnlinePage(id);
        return this.removeById(id);
    }

    /**
     * 删除在线表单数据表
     *
     * @param formId
     */
    private void deleteOnlineTable(Serializable formId) {
        LambdaQueryWrapper<AgileOnlineTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineTable::getFormId, formId);
        agileOnlineTableService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除在线表单数据表字段
     *
     * @param formId
     */
    private void deleteOnlineColumn(Serializable formId) {
        LambdaQueryWrapper<AgileOnlineColumn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineColumn::getFormId, formId);
        agileOnlineColumnService.remove(lambdaQueryWrapper);
    }
    /**
     * 删除在线表单数据表字段规则配置
     *
     * @param formId
     */
    private void deleteOnlineColumnRule(Serializable formId) {
        LambdaQueryWrapper<AgileOnlineColumnRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineColumnRule::getFormId, formId);
        agileOnlineColumnRuleService.remove(lambdaQueryWrapper);
    }
    /**
     * 删除在线表单数据表字段
     *
     * @param formId
     */
    private void deleteOnlinePage(Serializable formId) {
        LambdaQueryWrapper<AgileOnlinePage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlinePage::getFormId, formId);
        agileOnlinePageService.remove(lambdaQueryWrapper);
    }
}
