package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import com.jeeagile.frame.mapper.online.AgileOnlineColumnMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

/**
 * @author JeeAgile
 * @date 2021-07-27
 * @description 在线表单 表单数据表字段 接口实现
 */
@AgileService
public class AgileOnlineColumnServiceImpl extends AgileBaseServiceImpl<AgileOnlineColumnMapper, AgileOnlineColumn> implements IAgileOnlineColumnService {

    @Override
    public LambdaQueryWrapper<AgileOnlineColumn> queryWrapper(AgileOnlineColumn agileOnlineColumn) {
        if (agileOnlineColumn == null || AgileStringUtil.isEmpty(agileOnlineColumn.getFormId())) {
            throw new AgileValidateException("表单ID不能空！");
        }
        if (agileOnlineColumn == null || AgileStringUtil.isEmpty(agileOnlineColumn.getTableId())) {
            throw new AgileValidateException("数据表ID不能空！");
        }
        LambdaQueryWrapper<AgileOnlineColumn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineColumn::getFormId, agileOnlineColumn.getFormId());
        lambdaQueryWrapper.eq(AgileOnlineColumn::getTableId, agileOnlineColumn.getTableId());
        lambdaQueryWrapper.orderByAsc(AgileOnlineColumn::getColumnSort);
        return lambdaQueryWrapper;
    }
    @Override
    public boolean updateModel(AgileOnlineColumn agileOnlineColumn) {
        AgileOnlineColumn oldOnlineColumn = this.getById(agileOnlineColumn.getId());
        if (oldOnlineColumn == null || oldOnlineColumn.isEmptyPk()) {
            throw new AgileValidateException("字段已不存在无法更新!");
        }
        oldOnlineColumn.setFilterType(agileOnlineColumn.getFilterType());
        oldOnlineColumn.setFieldLabel(agileOnlineColumn.getFieldLabel());
        oldOnlineColumn.setDictId(agileOnlineColumn.getDictId());
        oldOnlineColumn.setFieldKind(agileOnlineColumn.getFieldKind());
        return this.updateById(oldOnlineColumn);
    }
}
