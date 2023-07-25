package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
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

    }
}
