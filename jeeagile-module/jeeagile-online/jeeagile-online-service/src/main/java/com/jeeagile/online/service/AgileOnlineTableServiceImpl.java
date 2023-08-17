package com.jeeagile.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import com.jeeagile.online.constants.OnlineFieldKind;
import com.jeeagile.online.constants.OnlinePageType;
import com.jeeagile.online.constants.OnlineTableType;
import com.jeeagile.frame.constants.SysYesNo;
import com.jeeagile.online.entity.AgileOnlineColumn;
import com.jeeagile.online.entity.AgileOnlinePage;
import com.jeeagile.online.entity.AgileOnlineTable;
import com.jeeagile.online.mapper.AgileOnlineTableMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.system.IAgileSysJdbcService;
import com.jeeagile.online.vo.OnlinePageTable;
import com.jeeagile.online.vo.OnlineTableColumn;
import com.jeeagile.frame.vo.system.AgileJdbcTableColumn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
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
    @Autowired
    private IAgileOnlinePageService agileOnlinePageService;
    @Autowired
    private IAgileOnlineDictService agileOnlineDictService;

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
            AgileOnlineColumn agileOnlineColumn = handleAgileOnlineColumn(agileOnlineTable, agileJdbcTableColumn);
            agileOnlineColumnService.saveModel(agileOnlineColumn);
            if (!OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
                if (agileOnlineTable.getSlaveColumnName().equals(agileOnlineColumn.getColumnName())) {
                    agileOnlineTable.setSlaveColumnId(agileOnlineColumn.getId());
                }
            }
            if (SysYesNo.YES.equals(agileOnlineColumn.getPrimaryFlag())) {
                agileOnlineTable.setPrimaryColumnId(agileOnlineColumn.getId());
                agileOnlineTable.setPrimaryColumnName(agileOnlineColumn.getColumnName());
            }
        });
    }

    /**
     * @param agileOnlineTable
     * @param agileJdbcTableColumn
     * @return
     */
    private AgileOnlineColumn handleAgileOnlineColumn(AgileOnlineTable agileOnlineTable, AgileJdbcTableColumn agileJdbcTableColumn) {
        AgileOnlineColumn agileOnlineColumn = new AgileOnlineColumn();
        BeanUtils.copyProperties(agileJdbcTableColumn, agileOnlineColumn);
        agileOnlineColumn.setFormId(agileOnlineTable.getFormId());
        agileOnlineColumn.setTableId(agileOnlineTable.getId());
        agileOnlineColumn.setFieldName(AgileStringUtil.toCamelCase(agileJdbcTableColumn.getColumnName()));
        agileOnlineColumn.setFieldLabel(agileJdbcTableColumn.getColumnComment());
        agileOnlineColumn.setFieldType(agileJdbcTableColumn.getJavaType());
        agileOnlineColumn.setFieldKind(OnlineFieldKind.convertfieldKind(agileJdbcTableColumn.getColumnName()));
        return agileOnlineColumn;
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
            if (AgileStringUtil.isEmpty(agileOnlineTable.getSlaveColumnName())) {
                throw new AgileValidateException("从表字段名称不能为空！");
            }
        }
    }

    @Override
    public AgileOnlineColumn addOnlineColumn(AgileOnlineColumn agileOnlineColumn) {
        AgileValidateUtil.validateObject(agileOnlineColumn);
        agileOnlineColumn = getAgileOnlineColumn(agileOnlineColumn.getTableId(), agileOnlineColumn.getColumnName());
        agileOnlineColumnService.saveModel(agileOnlineColumn);
        return agileOnlineColumn;
    }

    @Override
    public AgileOnlineColumn refreshOnlineColumn(AgileOnlineColumn agileOnlineColumn) {
        AgileValidateUtil.validateObject(agileOnlineColumn);
        if (AgileStringUtil.isEmpty(agileOnlineColumn.getColumnName())) {
            agileOnlineColumn = agileOnlineColumnService.getById(agileOnlineColumn.getId());
        }
        String onlineColumnId = agileOnlineColumn.getId();
        agileOnlineColumn = getAgileOnlineColumn(agileOnlineColumn.getTableId(), agileOnlineColumn.getColumnName());
        agileOnlineColumn.setId(onlineColumnId);
        agileOnlineColumnService.updateById(agileOnlineColumn);
        return agileOnlineColumn;
    }

    @Override
    public List<OnlinePageTable> pageTableList(String pageId) {
        AgileOnlinePage agileOnlinePage = agileOnlinePageService.getById(pageId);
        if (agileOnlinePage == null || agileOnlinePage.isEmptyPk()) {
            throw new AgileValidateException("该表单页面已不存在！");
        }
        AgileOnlineTable agileOnlineTable = this.getById(agileOnlinePage.getTableId());
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("该表单页面数据表已不存在！");
        }
        List<AgileOnlineTable> agileOnlineTableList = new ArrayList<>();
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
            AgileOnlineTable queryOnlineTable = AgileOnlineTable.builder().formId(agileOnlineTable.getFormId()).build();
            agileOnlineTableList = this.selectList(queryOnlineTable);
        } else {
            agileOnlineTableList.add(agileOnlineTable);
        }
        List<OnlinePageTable> onlinePageTableList = new ArrayList<>();
        agileOnlineTableList.forEach(onlineTable -> {
            if (OnlinePageType.QUERY.equals(agileOnlinePage.getPageType()) && OnlineTableType.ONE_TO_MANY.equals(onlineTable.getTableType())) {
                return;
            }
            OnlinePageTable onlinePageTable = new OnlinePageTable();
            BeanUtils.copyProperties(onlineTable, onlinePageTable);
            onlinePageTable.setTableId(onlineTable.getId());
            onlinePageTable.setTableColumnList(this.getOnlineTableColumn(onlinePageTable.getFormId(), onlinePageTable.getTableId()));
            onlinePageTableList.add(onlinePageTable);
        });
        return onlinePageTableList;
    }

    /**
     * 获取表字段
     *
     * @param fromId  表单ID
     * @param tableId 数据表ID
     * @return
     */
    private List<OnlineTableColumn> getOnlineTableColumn(String fromId, String tableId) {
        AgileOnlineColumn queryOnlineColumn = AgileOnlineColumn.builder().formId(fromId).tableId(tableId).build();
        List<AgileOnlineColumn> agileOnlineColumnList = agileOnlineColumnService.selectList(queryOnlineColumn);
        List<OnlineTableColumn> onlineTableColumnList = new ArrayList<>();
        agileOnlineColumnList.forEach(agileOnlineColumn -> {
            OnlineTableColumn onlineTableColumn = new OnlineTableColumn();
            onlineTableColumn.setColumnId(agileOnlineColumn.getId());
            BeanUtils.copyProperties(agileOnlineColumn, onlineTableColumn);
            if (AgileStringUtil.isNotEmpty(agileOnlineColumn.getDictId())) {
                onlineTableColumn.setOnlineDict(agileOnlineDictService.getById(agileOnlineColumn.getDictId()));
            }
            onlineTableColumnList.add(onlineTableColumn);
        });
        return onlineTableColumnList;
    }

    /**
     * @param tableId
     * @param columnName
     * @return
     */
    private AgileOnlineColumn getAgileOnlineColumn(String tableId, String columnName) {
        AgileOnlineTable agileOnlineTable = this.getById(tableId);
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据表已不存在！");
        }
        AgileJdbcTableColumn agileJdbcTableColumn = agileSysJdbcService.selectTableColumn(agileOnlineTable.getTableName(), columnName);
        if (agileJdbcTableColumn == null || AgileStringUtil.isEmpty(agileJdbcTableColumn.getColumnName())) {
            throw new AgileValidateException("字段已不存在！");
        }
        return handleAgileOnlineColumn(agileOnlineTable, agileJdbcTableColumn);
    }
}
