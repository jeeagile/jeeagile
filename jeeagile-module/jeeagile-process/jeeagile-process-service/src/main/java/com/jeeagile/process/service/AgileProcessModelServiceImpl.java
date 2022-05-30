package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getProcessCode())) {
                lambdaQueryWrapper.eq(AgileProcessModel::getProcessCode, agileProcessModel.getProcessCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getProcessName())) {
                lambdaQueryWrapper.like(AgileProcessModel::getProcessName, agileProcessModel.getProcessName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessModel.getProcessDeploymentStatus())) {
                lambdaQueryWrapper.eq(AgileProcessModel::getProcessDeploymentStatus, agileProcessModel.getProcessDeploymentStatus());
            }
        }
        return lambdaQueryWrapper;
    }

}
