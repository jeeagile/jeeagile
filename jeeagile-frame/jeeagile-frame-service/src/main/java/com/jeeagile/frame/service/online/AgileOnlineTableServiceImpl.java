package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.constants.online.OnlineFieldClassify;
import com.jeeagile.frame.constants.online.OnlineTableType;
import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import com.jeeagile.frame.entity.online.AgileOnlineTable;
import com.jeeagile.frame.mapper.online.AgileOnlineTableMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.system.IAgileSysJdbcService;
import com.jeeagile.frame.vo.system.AgileJdbcTableColumn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-07-25
 * @description 在线表单 表单数据模型 接口实现
 */
@AgileService
public class AgileOnlineTableServiceImpl extends AgileBaseServiceImpl<AgileOnlineTableMapper, AgileOnlineTable> implements IAgileOnlineTableService {

    @Autowired
    private IAgileSysJdbcService agileSysJdbcService;
    @Autowired
    private IAgileOnlineColumnService agileOnlineColumnService;

    @Override
    public LambdaQueryWrapper<AgileOnlineTable> queryWrapper(AgileOnlineTable agileOnlineTable) {
        if (agileOnlineTable == null || AgileStringUtil.isEmpty(agileOnlineTable.getFormId())) {
            throw new AgileValidateException("表单ID不能空！");
        }
        LambdaQueryWrapper<AgileOnlineTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineTable::getFormId, agileOnlineTable.getFormId());
        lambdaQueryWrapper.orderByAsc(AgileOnlineTable::getTableType);
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelBefore(AgileOnlineTable agileOnlineTable) {
        agileOnlineTable.setId(AgileStringUtil.getUuid());
        List<AgileJdbcTableColumn> agileJdbcTableColumnList = this.agileSysJdbcService.selectTableColumnList(agileOnlineTable.getTableName());
        agileJdbcTableColumnList.forEach(agileJdbcTableColumn -> {
            AgileOnlineColumn agileOnlineColumn = new AgileOnlineColumn();
            BeanUtils.copyProperties(agileJdbcTableColumn, agileOnlineColumn);
            agileOnlineColumn.setFormId(agileOnlineTable.getFormId());
            agileOnlineColumn.setTableId(agileOnlineTable.getId());
            agileOnlineColumn.setFieldName(AgileStringUtil.toCamelCase(agileJdbcTableColumn.getColumnName()));
            agileOnlineColumn.setFieldLabel(agileJdbcTableColumn.getColumnComment());
            agileOnlineColumn.setFieldType(agileJdbcTableColumn.getJavaType());
            agileOnlineColumn.setFieldClassify(OnlineFieldClassify.convertFieldClassify(agileJdbcTableColumn.getColumnName()));
            agileOnlineColumnService.saveModel(agileOnlineColumn);
            if (!OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
                if (agileOnlineTable.getSlaveColumnName().equals(agileOnlineColumn.getColumnName())) {
                    agileOnlineTable.setSlaveColumnId(agileOnlineColumn.getId());
                }
            }
        });
    }

    @Override
    public void saveModelValidate(AgileOnlineTable agileOnlineTable) {
        this.validateData(agileOnlineTable);
    }

    @Override
    public void updateModelValidate(AgileOnlineTable agileOnlineTable) {
        this.validateData(agileOnlineTable);
    }

    @Override
    public boolean deleteModel(Serializable id) {
        LambdaQueryWrapper<AgileOnlineColumn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineColumn::getTableId, id);
        agileOnlineColumnService.remove(lambdaQueryWrapper);
        return this.removeById(id);
    }

    /**
     * 校验数据模型
     */
    private void validateData(AgileOnlineTable agileOnlineTable) {
        if (!OnlineTableType.isValid(agileOnlineTable.getTableType())) {
            throw new AgileValidateException("非法数据表类型!");
        }
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
        } else {
            if (AgileStringUtil.isEmpty(agileOnlineTable.getMasterTableId())) {
                throw new AgileValidateException("主表ID不能为空！");
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
