package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessForm;
import com.jeeagile.process.entity.AgileProcessModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JeeAgile
 * @date 2022-06-10
 * @description 我的事务
 */
@AgileService
public class AgileProcessTaskServiceImpl implements IAgileProcessTaskService {
    @Autowired
    private IAgileProcessModelService agileProcessModelService;
    @Autowired
    private IAgileProcessDefinitionService agileProcessDefinitionService;

    @Override
    public AgilePage<AgileProcessDefinition> selectProcessDefinitionPage(AgilePageable<AgileProcessDefinition> agilePageable) {
        AgilePage<AgileProcessDefinition> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileProcessDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessDefinition.class, i -> !"modelXml".contains(i.getProperty()) || !"formFields".contains(i.getProperty()) || !"formConfig".contains(i.getProperty()));
        AgileProcessDefinition agileProcessDefinition = agilePageable.getQueryCond();
        if (agileProcessDefinition != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getModelCode())) {
                lambdaQueryWrapper.eq(AgileProcessDefinition::getModelCode, agileProcessDefinition.getModelCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getModelName())) {
                lambdaQueryWrapper.like(AgileProcessDefinition::getModelName, agileProcessDefinition.getModelName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getFormName())) {
                lambdaQueryWrapper.like(AgileProcessDefinition::getFormName, agileProcessDefinition.getFormName());
            }
        }
        lambdaQueryWrapper.eq(AgileProcessDefinition::getSuspensionState, 1);
        lambdaQueryWrapper.eq(AgileProcessDefinition::getMainVersion,1);
        lambdaQueryWrapper.orderByDesc(AgileProcessDefinition::getModelCode, AgileProcessDefinition::getModelVersion);
        return agileProcessDefinitionService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgileProcessDefinition getProcessDefinitionInfo(String processDefinitionId) {
        return agileProcessDefinitionService.selectModel(processDefinitionId);
    }
}
