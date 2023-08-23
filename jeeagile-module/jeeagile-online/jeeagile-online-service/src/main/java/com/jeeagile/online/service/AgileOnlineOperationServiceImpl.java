package com.jeeagile.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.online.constants.OnlineFieldKind;
import com.jeeagile.online.constants.OnlineTableType;
import com.jeeagile.core.constants.AgileYesNo;
import com.jeeagile.online.entity.AgileOnlineColumn;
import com.jeeagile.online.entity.AgileOnlineDict;
import com.jeeagile.online.entity.AgileOnlineTable;
import com.jeeagile.online.mapper.AgileOnlineOperationMapper;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.online.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author JeeAgile
 * @date 2023-08-14
 * @description 在线表单 表单操作 接口实现
 */
@AgileService
public class AgileOnlineOperationServiceImpl implements IAgileOnlineOperationService {
    @Autowired
    private AgileOnlineOperationMapper agileOnlineOperationMapper;
    @Autowired
    private IAgileOnlineDictService agileOnlineDictService;
    @Autowired
    private IAgileOnlineTableService agileOnlineTableService;
    @Autowired
    private IAgileOnlineColumnService agileOnlineColumnService;

    @Override
    public List<Map> selectDictData(OnlineQueryParam onlineQueryParam) {
        if (AgileStringUtil.isEmpty(onlineQueryParam.getDictId())) {
            throw new AgileValidateException("数据字典ID不能为空！");
        }
        AgileOnlineDict agileOnlineDict = agileOnlineDictService.getById(onlineQueryParam.getDictId());
        if (agileOnlineDict == null || agileOnlineDict.isEmptyPk()) {
            throw new AgileValidateException("数据字典已不存在！");
        }
        String selectFields = this.makeDictSelectField(agileOnlineDict, false);
        return agileOnlineOperationMapper.getDictDataList(agileOnlineDict.getTableName(), selectFields, onlineQueryParam.getFilterList());
    }

    @Override
    public AgilePage<Map> selectPageData(AgilePageable<OnlineQueryParam> agilePageable) {
        OnlineQueryParam onlineQueryParam = agilePageable.getQueryCond();
        if (onlineQueryParam == null || AgileStringUtil.isEmpty(onlineQueryParam.getTableId())) {
            throw new AgileValidateException("数据ID不能为空！");
        }
        AgileOnlineTable agileOnlineTable = agileOnlineTableService.getById(onlineQueryParam.getTableId());
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据表已不存在！");
        }

        Map<String, AgileOnlineTable> onlineTableMap = new HashMap();
        onlineTableMap.put(agileOnlineTable.getId(), agileOnlineTable);
        this.checkFieldFilter(agileOnlineTable, onlineTableMap, onlineQueryParam.getFilterList());
        String selectFields = this.makeTableSelectField(agileOnlineTable, onlineTableMap, onlineQueryParam.getQueryList());
        String orderBy = this.makeTableOrderBy(onlineTableMap, onlineQueryParam.getOrderList());
        AgilePage<Map> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        List<OnlineJoinTable> joinTableList = this.makeJoinTable(agileOnlineTable, onlineTableMap);
        return agileOnlineOperationMapper.getPageData(agilePage, agileOnlineTable.getTableName(), selectFields, joinTableList, onlineQueryParam.getFilterList(), orderBy);
    }

    @Override
    public Map selectOneData(String tableId, String dataId) {
        AgileOnlineTable agileOnlineTable = agileOnlineTableService.getById(tableId);
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据表已不存在！");
        }
        return this.getOneData(agileOnlineTable, dataId);
    }

    @Override
    public boolean saveTableData(String tableId, Map masterData, Map slaveData) {
        AgileOnlineTable agileOnlineTable = agileOnlineTableService.getById(tableId);
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据表已不存在！");
        }
        List<AgileOnlineColumn> tableColumnList = this.getOnlineColumnList(agileOnlineTable);
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType()) && AgileStringUtil.isNotEmpty(slaveData)) {
            List<OnlineColumnData> masterTableColumnDataList = this.makeColumnData(tableColumnList, masterData);
            this.saveTableData(agileOnlineTable, masterTableColumnDataList);
            if (AgileStringUtil.isNotEmpty(slaveData)) {
                for (Object key : slaveData.keySet()) {
                    AgileOnlineTable slaveOnlineTable = this.agileOnlineTableService.getById(key.toString());
                    if (slaveOnlineTable == null || slaveOnlineTable.isEmptyPk()) {
                        continue;
                    }
                    OnlineColumnData masterTableColumnData = masterTableColumnDataList.stream().filter(onlineTableColumn -> onlineTableColumn.getId().equals(slaveOnlineTable.getMasterColumnId())).findFirst().get();
                    if (masterTableColumnData == null || masterTableColumnData.isEmptyPk()) {
                        continue;
                    }
                    if (AgileStringUtil.isEmpty(masterTableColumnData.getColumnValue())) {
                        throw new AgileValidateException("主表关联字段值不能为空！");
                    }
                    List<AgileOnlineColumn> slaveOnlineColumnList = this.getOnlineColumnList(slaveOnlineTable);
                    AgileOnlineColumn slaveTableColumn = slaveOnlineColumnList.stream().filter(onlineTableColumn -> onlineTableColumn.getId().equals(slaveOnlineTable.getSlaveColumnId())).findFirst().get();
                    if (slaveTableColumn == null || slaveTableColumn.isEmptyPk()) {
                        continue;
                    }
                    if (OnlineTableType.ONE_TO_ONE.equals(slaveOnlineTable.getTableType())) {
                        Map slaveTableData = (Map) slaveData.get(key);
                        slaveTableData.put(slaveTableColumn.getColumnName(), masterTableColumnData.getColumnValue());
                        List<OnlineColumnData> slaveTableColumnDataList = this.makeColumnData(slaveOnlineColumnList, slaveTableData);
                        this.saveTableData(slaveOnlineTable, slaveTableColumnDataList);
                    } else {
                        List<Map> slaveTableDataList = (List<Map>) slaveData.get(key);
                        for (Map slaveDataMap : slaveTableDataList) {
                            slaveDataMap.put(slaveTableColumn.getColumnName(), masterTableColumnData.getColumnValue());
                            List<OnlineColumnData> slaveTableColumnDataList = this.makeColumnData(slaveOnlineColumnList, slaveDataMap);
                            this.saveTableData(slaveOnlineTable, slaveTableColumnDataList);
                        }
                    }
                }
            }
        } else {
            List<OnlineColumnData> slaveTableColumnDataList = this.makeColumnData(tableColumnList, slaveData);
            this.saveTableData(agileOnlineTable, slaveTableColumnDataList);
        }
        return true;
    }

    @Override
    public boolean updateTableData(String tableId, Map masterData, Map slaveData) {
        AgileOnlineTable agileOnlineTable = this.agileOnlineTableService.getById(tableId);
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据从表不存在！");
        }
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
            if (AgileStringUtil.isNotEmpty(slaveData)) {
                for (Object key : slaveData.keySet()) {
                    AgileOnlineTable slaveOnlineTable = this.agileOnlineTableService.getById(key.toString());
                    if (slaveOnlineTable == null || slaveOnlineTable.isEmptyPk()) {
                        continue;
                    }
                    this.updateTableData(slaveOnlineTable, (Map) slaveData.get(key));
                }
            }
            return this.updateTableData(agileOnlineTable, masterData);
        } else {
            return this.updateTableData(agileOnlineTable, slaveData);
        }
    }

    @Override
    public boolean deleteTableData(String tableId, String dataId) {
        AgileOnlineTable agileOnlineTable = agileOnlineTableService.getById(tableId);
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据表已不存在！");
        }
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) { // 删除从表数据
            List<AgileOnlineTable> slaveOnlineTableList = getSlaveOnlineTable(agileOnlineTable.getFormId(), agileOnlineTable.getId());
            if (AgileStringUtil.isNotEmpty(slaveOnlineTableList)) {
                Map masterTableData = getOneData(agileOnlineTable, dataId);
                if (AgileStringUtil.isNotEmpty(masterTableData)) {
                    for (AgileOnlineTable subOnlineTable : slaveOnlineTableList) {
                        List<OnlineFieldFilter> filterList = new ArrayList<>();
                        AgileOnlineColumn masterOnlineColumn = this.agileOnlineColumnService.getById(subOnlineTable.getMasterColumnId());
                        filterList.add(this.makeTableFieldFilter(subOnlineTable.getTableName(), subOnlineTable.getSlaveColumnName(), masterTableData.get(masterOnlineColumn.getFieldName())));
                        agileOnlineOperationMapper.delete(subOnlineTable.getTableName(), filterList);
                    }
                }
            }
        }
        List<OnlineFieldFilter> filterList = new ArrayList<>();
        filterList.add(this.makeTableFieldFilter(agileOnlineTable.getTableName(), agileOnlineTable.getPrimaryColumnName(), dataId));
        return agileOnlineOperationMapper.delete(agileOnlineTable.getTableName(), filterList) > 0;
    }

    /**
     * 组装字典查询字段
     *
     * @param agileOnlineDict
     * @param ignoreParentId
     * @return
     */
    private String makeDictSelectField(AgileOnlineDict agileOnlineDict, boolean ignoreParentId) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(agileOnlineDict.getKeyColumnName()).append(" id, ");
        sb.append(agileOnlineDict.getValueColumnName()).append(" dictValue, ");
        sb.append(agileOnlineDict.getLabelColumnName()).append(" dictLabel");
        if (!ignoreParentId && AgileYesNo.YES.equals(agileOnlineDict.getTreeFlag())) {
            sb.append(", ").append(agileOnlineDict.getParentKeyColumnName()).append(" parentId");
        }
        return sb.toString();
    }

    /**
     * 校验过滤条件
     *
     * @param onlineTableMap
     * @param fieldFilterList
     */
    private void checkFieldFilter(AgileOnlineTable agileOnlineTable, Map<String, AgileOnlineTable> onlineTableMap, List<OnlineFieldFilter> fieldFilterList) {
        // 如果为从表 校验过滤条件是否存在关联字段 避免全表查询
        if (!OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
            if (!fieldFilterList.stream().anyMatch(onlineFieldFilter -> onlineFieldFilter.getColumnId().equals(agileOnlineTable.getSlaveColumnId()))) {
                throw new AgileValidateException("缺少从表关联字段查询条件！");
            }
        }

        StringBuffer errorMessage = new StringBuffer();
        for (OnlineFieldFilter onlineFieldFilter : fieldFilterList) {
            AgileOnlineColumn agileOnlineColumn = this.agileOnlineColumnService.getById(onlineFieldFilter.getColumnId());
            if (agileOnlineColumn == null || agileOnlineColumn.isEmptyPk()) {
                errorMessage.append("过滤条件条件，过滤字段《").append(onlineFieldFilter.getColumnName()).append("》已不存在！\r\n");
                continue;
            } else {
                onlineFieldFilter.setTableId(agileOnlineColumn.getTableId());
                onlineFieldFilter.setColumnName(agileOnlineColumn.getColumnName());
            }
            AgileOnlineTable filterOnlineTable = null;
            if (onlineTableMap.containsKey(onlineFieldFilter.getTableId())) {
                filterOnlineTable = onlineTableMap.get(onlineFieldFilter.getTableId());
            } else {
                filterOnlineTable = this.agileOnlineTableService.getById(onlineFieldFilter.getTableId());
            }
            if (filterOnlineTable == null || filterOnlineTable.isEmptyPk()) {
                errorMessage.append("过滤条件条件，过滤表《").append(onlineFieldFilter.getTableName()).append("》已不存在！\r\n");
                continue;
            } else {
                onlineTableMap.put(filterOnlineTable.getId(), filterOnlineTable);
                onlineFieldFilter.setTableName(filterOnlineTable.getTableName());
            }
        }
        if (AgileStringUtil.isNotEmpty(errorMessage)) {
            throw new AgileValidateException(errorMessage.toString());
        }
    }

    /**
     * 拼装分页查询字段
     *
     * @param queryFieldList
     * @return
     */
    private String makeTableSelectField(AgileOnlineTable agileOnlineTable, Map<String, AgileOnlineTable> onlineTableMap, List<OnlineFieldQuery> queryFieldList) {
        List<OnlineFieldQuery> newQueryFieldList = new ArrayList<>();
        if (AgileStringUtil.isEmpty(queryFieldList)) queryFieldList = new ArrayList<>();
        for (OnlineFieldQuery onlineFieldQuery : queryFieldList) {
            AgileOnlineColumn agileOnlineColumn = this.agileOnlineColumnService.getById(onlineFieldQuery.getColumnId());
            if (agileOnlineColumn == null || agileOnlineColumn.isEmptyPk()) {
                continue;
            }
            onlineFieldQuery.setTableId(agileOnlineColumn.getTableId());
            onlineFieldQuery.setColumnName(agileOnlineColumn.getColumnName());
            onlineFieldQuery.setFieldName(agileOnlineColumn.getFieldName());
            AgileOnlineTable queryOnlineTable = null;
            if (onlineTableMap.containsKey(onlineFieldQuery.getTableId())) {
                queryOnlineTable = onlineTableMap.get(onlineFieldQuery.getTableId());
            } else {
                queryOnlineTable = this.agileOnlineTableService.getById(onlineFieldQuery.getTableId());
            }
            if (queryOnlineTable != null && queryOnlineTable.isNotEmptyPk()) {
                onlineTableMap.put(queryOnlineTable.getId(), queryOnlineTable);
                onlineFieldQuery.setTableName(queryOnlineTable.getTableName());
                onlineFieldQuery.setTableType(queryOnlineTable.getTableType());
                onlineFieldQuery.setModelName(queryOnlineTable.getModelName());
                newQueryFieldList.add(onlineFieldQuery);
            }
        }
        // 添加主表主键查询字段
        if (!newQueryFieldList.stream().anyMatch(queryField -> queryField.getColumnId().equals(agileOnlineTable.getPrimaryColumnId()))) {
            OnlineFieldQuery onlineFieldQuery = makeTableQueryField(agileOnlineTable, agileOnlineTable.getPrimaryColumnId());
            if (onlineFieldQuery != null) newQueryFieldList.add(onlineFieldQuery);
        }

        // 添加主表关联字段
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
            List<AgileOnlineTable> slaveOnlineTableList = this.getSlaveOnlineTable(agileOnlineTable.getFormId(), agileOnlineTable.getId());
            slaveOnlineTableList.forEach(slaveOnlineTable -> {
                if (newQueryFieldList.stream().anyMatch(queryField -> queryField.getColumnId().equals(slaveOnlineTable.getSlaveColumnId()))) {
                    OnlineFieldQuery onlineFieldQuery = makeTableQueryField(agileOnlineTable, agileOnlineTable.getMasterColumnId());
                    if (onlineFieldQuery != null) newQueryFieldList.add(onlineFieldQuery);
                }
            });
        }
        // 拼装查询字段
        StringBuilder selectFieldBuilder = new StringBuilder();
        newQueryFieldList.forEach(onlineFieldQuery -> {
            selectFieldBuilder.append(onlineFieldQuery.getTableName()).append(".");
            selectFieldBuilder.append(onlineFieldQuery.getColumnName()).append(' ');
            if (!OnlineTableType.MASTER.equals(onlineFieldQuery.getTableType())) {
                selectFieldBuilder.append(onlineFieldQuery.getModelName()).append("__");
            }
            selectFieldBuilder.append(onlineFieldQuery.getFieldName()).append(",");
        });
        return selectFieldBuilder.substring(0, selectFieldBuilder.length() - 1);
    }

    /**
     * 组装查询条件
     *
     * @param agileOnlineTable
     * @param columnId
     * @return
     */
    private OnlineFieldQuery makeTableQueryField(AgileOnlineTable agileOnlineTable, String columnId) {
        AgileOnlineColumn agileOnlineColumn = this.agileOnlineColumnService.getById(columnId);
        if (agileOnlineColumn != null && agileOnlineColumn.isNotEmptyPk()) {
            OnlineFieldQuery onlineFieldQuery = new OnlineFieldQuery();
            BeanUtils.copyProperties(agileOnlineColumn, onlineFieldQuery);
            BeanUtils.copyProperties(agileOnlineTable, onlineFieldQuery);
            onlineFieldQuery.setTableId(agileOnlineTable.getId());
            onlineFieldQuery.setColumnId(agileOnlineColumn.getId());
            return onlineFieldQuery;
        }
        return null;
    }

    /**
     * 拼装数据表查询字段
     *
     * @param agileOnlineTable
     * @return
     */
    private String makeTableSelectField(AgileOnlineTable agileOnlineTable) {
        StringBuilder selectFieldBuilder = new StringBuilder();
        getOnlineColumnList(agileOnlineTable).forEach(agileOnlineColumn -> {
            selectFieldBuilder.append(agileOnlineTable.getTableName()).append(".");
            selectFieldBuilder.append(agileOnlineColumn.getColumnName()).append(' ');
            if (!OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
                selectFieldBuilder.append(agileOnlineTable.getModelName()).append("__");
            }
            selectFieldBuilder.append(agileOnlineColumn.getFieldName()).append(",");
        });
        return selectFieldBuilder.substring(0, selectFieldBuilder.length() - 1);
    }

    /**
     * 组装字段过滤条件
     *
     * @param tableName   数据名
     * @param columnName  字段名
     * @param columnValue 值
     * @return
     */
    private OnlineFieldFilter makeTableFieldFilter(String tableName, String columnName, Object columnValue) {
        OnlineFieldFilter onlineFieldFilter = new OnlineFieldFilter();
        onlineFieldFilter.setTableName(tableName);
        onlineFieldFilter.setColumnName(columnName);
        onlineFieldFilter.setColumnValue(columnValue);
        return onlineFieldFilter;
    }

    /**
     * 组装排序字段
     *
     * @param onlineTableMap
     * @param fieldOrderList
     */
    private String makeTableOrderBy(Map<String, AgileOnlineTable> onlineTableMap, List<OnlineFieldOrder> fieldOrderList) {
        List<OnlineFieldOrder> newFieldOrderList = new ArrayList<>();
        if (AgileStringUtil.isEmpty(fieldOrderList)) return null;
        for (OnlineFieldOrder onlineFieldOrder : fieldOrderList) {
            AgileOnlineColumn agileOnlineColumn = this.agileOnlineColumnService.getById(onlineFieldOrder.getColumnId());
            if (agileOnlineColumn == null || agileOnlineColumn.isEmptyPk()) {
                continue;
            }
            onlineFieldOrder.setColumnName(agileOnlineColumn.getColumnName());
            onlineFieldOrder.setFieldName(agileOnlineColumn.getFieldName());
            onlineFieldOrder.setTableId(agileOnlineColumn.getTableId());
            AgileOnlineTable agileOnlineTable = null;
            if (onlineTableMap.containsKey(onlineFieldOrder.getTableId())) {
                agileOnlineTable = onlineTableMap.get(onlineFieldOrder.getTableId());
            } else {
                agileOnlineTable = this.agileOnlineTableService.getById(onlineFieldOrder.getTableId());
            }
            if (agileOnlineTable != null && agileOnlineTable.isNotEmptyPk()) {
                BeanUtils.copyProperties(agileOnlineTable, onlineFieldOrder);
                newFieldOrderList.add(onlineFieldOrder);
            }
        }
        StringBuilder orderFieldBuilder = new StringBuilder();
        newFieldOrderList.forEach(onlineFieldOrder -> {
            orderFieldBuilder.append(onlineFieldOrder.getTableName()).append(".");
            orderFieldBuilder.append(onlineFieldOrder.getColumnName()).append(' ');
            if (!onlineFieldOrder.isAsc()) {
                orderFieldBuilder.append(" DESC,");
            }
        });
        return orderFieldBuilder.substring(0, orderFieldBuilder.length() - 1);
    }

    /**
     * 添加关联表
     *
     * @param onlineTableMap
     * @return
     */
    private List<OnlineJoinTable> makeJoinTable(AgileOnlineTable agileOnlineTable, Map<String, AgileOnlineTable> onlineTableMap) {
        List<OnlineJoinTable> joinTableList = new ArrayList<>();
        if (!OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) return joinTableList;
        onlineTableMap.values().forEach(slaveOnlineTable -> {
            if (OnlineTableType.ONE_TO_ONE.equals(slaveOnlineTable.getTableType())) {
                OnlineJoinTable onlineJoinTable = new OnlineJoinTable();
                onlineJoinTable.setJoinTableName(slaveOnlineTable.getTableName());
                AgileOnlineTable masterOnlineTable = onlineTableMap.get(slaveOnlineTable.getMasterTableId());
                onlineJoinTable.setLeftJoin(AgileYesNo.YES.equals(slaveOnlineTable.getLeftJoin()));
                onlineJoinTable.setJoinCondition(this.makeJoinCondition(masterOnlineTable, slaveOnlineTable));
                joinTableList.add(onlineJoinTable);
            }
        });
        return joinTableList;
    }

    /**
     * 拼装关联条件
     *
     * @param masterOnlineTable
     * @param slaveOnlineTable
     * @return
     */
    private String makeJoinCondition(AgileOnlineTable masterOnlineTable, AgileOnlineTable slaveOnlineTable) {
        StringBuilder conditionBuilder = new StringBuilder(64);
        conditionBuilder
                .append(masterOnlineTable.getTableName())
                .append(".")
                .append(slaveOnlineTable.getMasterColumnName())
                .append(" = ")
                .append(slaveOnlineTable.getTableName())
                .append(".")
                .append(slaveOnlineTable.getSlaveColumnName());
        return conditionBuilder.toString();
    }

    /**
     * 根据主键获取主表数据
     *
     * @param agileOnlineTable
     * @param dataId
     * @return
     */
    private Map getOneData(AgileOnlineTable agileOnlineTable, Object dataId) {
        if (AgileStringUtil.isEmpty(dataId)) {
            throw new AgileValidateException("数据主键值不能为空！");
        }
        String selectFields = this.makeTableSelectField(agileOnlineTable);
        List<OnlineJoinTable> joinTableList = null;
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) {
            List<AgileOnlineTable> slaveOnlineTableList = this.getSlaveOnlineTable(agileOnlineTable.getFormId(), agileOnlineTable.getId());
            Map<String, AgileOnlineTable> onlineTableMap = new HashMap<>();
            onlineTableMap.put(agileOnlineTable.getId(), agileOnlineTable);
            StringBuilder slaveSelectFields = new StringBuilder();
            for (AgileOnlineTable slaveOnlineTable : slaveOnlineTableList) {
                onlineTableMap.put(slaveOnlineTable.getId(), slaveOnlineTable);
                if (OnlineTableType.ONE_TO_ONE.equals(slaveOnlineTable.getTableType())) { // 添加从表查询字段
                    slaveSelectFields.append(",").append(this.makeTableSelectField(slaveOnlineTable));
                }
            }
            selectFields = selectFields + slaveSelectFields.toString();
            joinTableList = this.makeJoinTable(agileOnlineTable, onlineTableMap);
        }

        List<OnlineFieldFilter> fieldFilterList = new ArrayList<>();
        fieldFilterList.add(this.makeTableFieldFilter(agileOnlineTable.getTableName(), agileOnlineTable.getPrimaryColumnName(), dataId));
        return agileOnlineOperationMapper.getOneData(agileOnlineTable.getTableName(), selectFields, joinTableList, fieldFilterList);
    }

    /**
     * 保存表数据
     *
     * @param agileOnlineTable     数据表信息
     * @param onlineColumnDataList 表字段值信息
     */
    private void saveTableData(AgileOnlineTable agileOnlineTable, List<OnlineColumnData> onlineColumnDataList) {
        List<Object> columnValueList = new LinkedList<>();
        String columnName = this.makeColumnName(onlineColumnDataList);
        for (OnlineColumnData onlineColumnData : onlineColumnDataList) {
            columnValueList.add(onlineColumnData.getColumnValue());
        }
        agileOnlineOperationMapper.insert(agileOnlineTable.getTableName(), columnName, columnValueList);
    }

    private boolean updateTableData(AgileOnlineTable agileOnlineTable, Map tableData) {
        List<AgileOnlineColumn> tableColumnList = this.getOnlineColumnList(agileOnlineTable);
        List<OnlineColumnData> tableColumnDataList = this.makeColumnData(tableColumnList, tableData);
        List<OnlineColumnData> updateColumnList = new LinkedList<>();
        List<OnlineColumnData> whereColumnList = new LinkedList<>();
        for (OnlineColumnData columnData : tableColumnDataList) {
            if (AgileYesNo.YES.equals(columnData.getPrimaryFlag())) {
                whereColumnList.add(columnData);
                continue;
            }
            if (!OnlineFieldKind.CREATE_USER.equals(columnData.getFieldKind())
                    && !OnlineFieldKind.CREATE_TIME.equals(columnData.getFieldKind())) {
                updateColumnList.add(columnData);
            }
        }
        if (AgileStringUtil.isEmpty(updateColumnList)) {
            return true;
        }
        return agileOnlineOperationMapper.update(agileOnlineTable.getTableName(), updateColumnList, whereColumnList) == 1;
    }

    /**
     * 查询关联从表
     *
     * @param formId  表单ID
     * @param tableId 主表ID
     * @return
     */
    private List<AgileOnlineTable> getSlaveOnlineTable(String formId, String tableId) {
        LambdaQueryWrapper<AgileOnlineTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineTable::getFormId, formId);
        lambdaQueryWrapper.eq(AgileOnlineTable::getMasterTableId, tableId);
        return this.agileOnlineTableService.list(lambdaQueryWrapper);
    }

    /**
     * 获取数据表对应字段
     *
     * @param agileOnlineTable 数据表信息
     * @return
     */
    private List<AgileOnlineColumn> getOnlineColumnList(AgileOnlineTable agileOnlineTable) {
        LambdaQueryWrapper<AgileOnlineColumn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineColumn::getFormId, agileOnlineTable.getFormId());
        lambdaQueryWrapper.eq(AgileOnlineColumn::getTableId, agileOnlineTable.getId());
        return agileOnlineColumnService.list(lambdaQueryWrapper);
    }

    /**
     * 组装字段名称
     *
     * @param columnDataList
     * @return
     */
    private String makeColumnName(List<OnlineColumnData> columnDataList) {
        StringBuilder stringBuilder = new StringBuilder(512);
        for (OnlineColumnData onlineColumnData : columnDataList) {
            stringBuilder.append(onlineColumnData.getColumnName()).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * 组装字段数据
     *
     * @return
     */
    private List<OnlineColumnData> makeColumnData(List<AgileOnlineColumn> onlineColumnList, Map tableData) {
        List<OnlineColumnData> onlineColumnDataList = new ArrayList<>();
        onlineColumnList.forEach(onlineColumn -> {
            OnlineColumnData onlineColumnData = new OnlineColumnData();
            BeanUtils.copyProperties(onlineColumn, onlineColumnData);
            onlineColumnData.setColumnValue(tableData.get(onlineColumnData.getColumnName()));
            if (AgileYesNo.NO.equals(onlineColumnData.getColumnNullable()) && AgileStringUtil.isEmpty(onlineColumnData.getColumnValue())) {
                onlineColumnData.setColumnValue(onlineColumnData.getColumnDefault());
            }
            this.makeColumnValue(onlineColumnData);
            onlineColumnDataList.add(onlineColumnData);
        });
        return onlineColumnDataList;
    }

    /**
     * 设置表字段值
     *
     * @param onlineColumnData
     */
    private void makeColumnValue(OnlineColumnData onlineColumnData) {
        if (AgileYesNo.YES.equals(onlineColumnData.getPrimaryFlag())) {
            if (onlineColumnData.getColumnValue() == null) {
                if ("String".equals(onlineColumnData.getFieldType())) {
                    onlineColumnData.setColumnValue(AgileStringUtil.getUuid());
                } else {

                }
            }
        } else if (onlineColumnData.getFieldKind() != null) {
            switch (onlineColumnData.getFieldKind()) {
                case OnlineFieldKind.CREATE_TIME: //创建时间字段
                case OnlineFieldKind.UPDATE_TIME: //更新时间字段
                    onlineColumnData.setColumnValue(new Date());
                    break;
                case OnlineFieldKind.CREATE_USER: //创建人字段
                case OnlineFieldKind.UPDATE_USER: //更新人字段
                    onlineColumnData.setColumnValue(AgileSecurityContext.getUserId());
                    break;
                case OnlineFieldKind.LOGIC_DELETE: //逻辑删除字段
                    onlineColumnData.setColumnValue(AgileYesNo.NO);
                    break;
                default:
                    break;
            }
        }
    }

}
