package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessExpression;
import com.jeeagile.process.mapper.AgileProcessExpressionMapper;

/**
 * @author JeeAgile
 * @date 2022-08-08
 * @description 流程表达式
 */
@AgileService
public class AgileProcessExpressionServiceImpl extends AgileBaseServiceImpl<AgileProcessExpressionMapper, AgileProcessExpression> implements IAgileProcessExpressionService {
    @Override
    public LambdaQueryWrapper<AgileProcessExpression> queryWrapper(AgileProcessExpression agileProcessExpression) {
        LambdaQueryWrapper<AgileProcessExpression> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileProcessExpression != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessExpression.getExpressionCode())) {
                lambdaQueryWrapper.eq(AgileProcessExpression::getExpressionCode, agileProcessExpression.getExpressionCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessExpression.getExpressionName())) {
                lambdaQueryWrapper.eq(AgileProcessExpression::getExpressionName, agileProcessExpression.getExpressionName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessExpression.getExpressionValue())) {
                lambdaQueryWrapper.like(AgileProcessExpression::getExpressionValue, agileProcessExpression.getExpressionValue());
            }
        }
        lambdaQueryWrapper.orderByDesc(AgileProcessExpression::getExpressionCode);
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileProcessExpression agileProcessExpression) {
        this.validateProcessForm(agileProcessExpression);
    }

    @Override
    public void updateModelValidate(AgileProcessExpression agileProcessExpression) {
        this.validateProcessForm(agileProcessExpression);
    }

    /**
     * 校验编码和名称是否存在
     */
    private void validateProcessForm(AgileProcessExpression agileProcessExpression) {
        LambdaQueryWrapper<AgileProcessExpression> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileProcessExpression.getId() != null) {
            lambdaQueryWrapper.ne(AgileProcessExpression::getId, agileProcessExpression.getId());
        }
        lambdaQueryWrapper.and(queryWrapper ->
                queryWrapper.eq(AgileProcessExpression::getExpressionCode, agileProcessExpression.getExpressionCode()).or().eq(AgileProcessExpression::getExpressionName, agileProcessExpression.getExpressionName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("流程表单编码或表单名称已存在！");
        }
    }
}
