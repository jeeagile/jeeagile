package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.constants.online.OnlineTableType;
import com.jeeagile.frame.entity.online.AgileOnlineTable;
import com.jeeagile.frame.mapper.online.AgileOnlineTableMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

/**
 * @author JeeAgile
 * @date 2021-07-25
 * @description 在线表单 表单数据模型 接口实现
 */
@AgileService
public class AgileOnlineTableServiceImpl extends AgileBaseServiceImpl<AgileOnlineTableMapper, AgileOnlineTable> implements IAgileOnlineTableService {

    @Override
    public LambdaQueryWrapper<AgileOnlineTable> queryWrapper(AgileOnlineTable agileOnlineTable) {
        if (agileOnlineTable == null || AgileStringUtil.isEmpty(agileOnlineTable.getFormId())) {
            throw new AgileValidateException("表单ID不能空！");
        }
        LambdaQueryWrapper<AgileOnlineTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineTable::getFormId, agileOnlineTable.getFormId());
        lambdaQueryWrapper.orderByDesc(AgileOnlineTable::getTableType);
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileOnlineTable agileOnlineTable) {
        this.validateData(agileOnlineTable);
    }

    @Override
    public void updateModelValidate(AgileOnlineTable agileOnlineTable) {
        this.validateData(agileOnlineTable);
    }

    /**
     * 校验数据模型
     */
    private void validateData(AgileOnlineTable agileOnlineTable) {
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
            LambdaQueryWrapper<AgileOnlineTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileOnlineTable::getFormId, agileOnlineTable.getFormId());
            lambdaQueryWrapper.eq(AgileOnlineTable::getTableType, OnlineTableType.MASTER);
            if (AgileStringUtil.isNotEmpty(agileOnlineTable.getId())) {
                lambdaQueryWrapper.ne(AgileOnlineTable::getId, agileOnlineTable.getId());
            }
            if (this.count(lambdaQueryWrapper) > 0) {
                throw new AgileValidateException("在线表单数据主表已存在不能重复添加!");
            }
        }
        if (!OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
            if (OnlineTableType.isValid(agileOnlineTable.getTableType())) {
                throw new AgileValidateException("非法数据表类型!");
            }
            if (AgileStringUtil.isEmpty(agileOnlineTable.getMasterColumnId())) {
                throw new AgileValidateException("主表字段ID不能为空！");
            }
            if (AgileStringUtil.isEmpty(agileOnlineTable.getMasterColumnName())) {
                throw new AgileValidateException("主表字段名称不能为空！");
            }
            // todo 根据字段ID获取字典信息校验字段名称是否正确
            if (AgileStringUtil.isEmpty(agileOnlineTable.getSlaveColumnName())) {
                throw new AgileValidateException("从表字段名称不能为空！");
            }
        }
    }
}
