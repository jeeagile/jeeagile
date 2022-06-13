package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.mapper.AgileProcessDefinitionMapper;
import com.jeeagile.process.support.IAgileProcessService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JeeAgile
 * @date 2022-06-06
 * @description 流程定义
 */
@AgileService
public class AgileProcessDefinitionServiceImpl extends AgileBaseServiceImpl<AgileProcessDefinitionMapper, AgileProcessDefinition> implements IAgileProcessDefinitionService {
    @Autowired
    private IAgileProcessService agileProcessService;

    @Override
    public LambdaQueryWrapper<AgileProcessDefinition> queryWrapper(AgileProcessDefinition agileProcessDefinition) {
        LambdaQueryWrapper<AgileProcessDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessDefinition.class, i -> !"modelXml".contains(i.getProperty()) || !"formFields".contains(i.getProperty()) || !"formConfig".contains(i.getProperty()));
        if (agileProcessDefinition != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getModelId())) {
                lambdaQueryWrapper.eq(AgileProcessDefinition::getModelId, agileProcessDefinition.getModelId());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getModelCode())) {
                lambdaQueryWrapper.eq(AgileProcessDefinition::getModelCode, agileProcessDefinition.getModelCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDefinition.getModelName())) {
                lambdaQueryWrapper.like(AgileProcessDefinition::getModelName, agileProcessDefinition.getModelName());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileProcessDefinition::getModelVersion);
        return lambdaQueryWrapper;
    }

    @Override
    public boolean processDefinitionActive(String id) {
        AgileProcessDefinition agileProcessDefinition = this.getById(id);
        if (agileProcessDefinition == null && agileProcessDefinition.isEmptyPk()) {
            throw new AgileFrameException("流程定义已不存在！");
        }
        agileProcessService.processDefinitionActive(agileProcessDefinition.getDefinitionId());
        agileProcessDefinition.setSuspensionState(1);
        agileProcessDefinition.setUpdateTime(null);
        agileProcessDefinition.setUpdateUser(null);
        return this.updateById(agileProcessDefinition);
    }

    @Override
    public boolean processDefinitionSuspend(String id) {
        AgileProcessDefinition agileProcessDefinition = this.getById(id);
        if (agileProcessDefinition == null && agileProcessDefinition.isEmptyPk()) {
            throw new AgileFrameException("流程定义已不存在！");
        }
        agileProcessService.processDefinitionSuspend(agileProcessDefinition.getDefinitionId());
        agileProcessDefinition.setSuspensionState(2);
        return this.updateById(agileProcessDefinition);
    }

    @Override
    public boolean updateMainVersion(String id) {
        AgileProcessDefinition agileProcessDefinition = this.getById(id);
        if (agileProcessDefinition != null && agileProcessDefinition.isNotEmptyPk()) {
            this.updateNoMainVersion(agileProcessDefinition.getModelId());
            agileProcessDefinition.setMainVersion(1);
            return this.updateById(agileProcessDefinition);
        }
        return false;
    }

    @Override
    public AgileProcessDefinition saveModel(AgileProcessDefinition agileProcessDefinition) {
        this.updateNoMainVersion(agileProcessDefinition.getModelId());
        //设置新流程定义为主版本
        agileProcessDefinition.setMainVersion(1);
        this.save(agileProcessDefinition);
        return agileProcessDefinition;
    }

    public void updateNoMainVersion(String modelId) {
        //将老版本更新为非主版本
        LambdaUpdateWrapper<AgileProcessDefinition> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileProcessDefinition::getMainVersion, "2");
        lambdaUpdateWrapper.eq(AgileProcessDefinition::getModelId, modelId);
        this.update(lambdaUpdateWrapper);
    }
}
