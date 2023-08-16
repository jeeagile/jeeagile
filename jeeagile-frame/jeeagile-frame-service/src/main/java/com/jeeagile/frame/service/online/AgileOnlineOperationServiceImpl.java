package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.constants.online.OnlineFieldKind;
import com.jeeagile.frame.constants.online.OnlineTableType;
import com.jeeagile.frame.constants.system.SysYesNo;
import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import com.jeeagile.frame.entity.online.AgileOnlineDict;
import com.jeeagile.frame.entity.online.AgileOnlineTable;
import com.jeeagile.frame.mapper.online.AgileOnlineOperationMapper;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.vo.online.OnlineColumnData;
import com.jeeagile.frame.vo.online.OnlineFieldFilter;
import com.jeeagile.frame.vo.online.OnlineQueryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author JeeAgile
 * @date 2021-08-14
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
        String selectFields = this.makeTableSelectField(agileOnlineTable);
        AgilePage<Map> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return agileOnlineOperationMapper.getPageData(agilePage, agileOnlineTable.getTableName(), selectFields, null, onlineQueryParam.getFilterList(), null);
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
                List<Map> slaveTableDataList = (List<Map>) slaveData.get(key);
                for (Map slaveDataMap : slaveTableDataList) {
                    slaveDataMap.put(slaveTableColumn.getColumnName(), masterTableColumnData.getColumnValue());
                    List<OnlineColumnData> slaveTableColumnDataList = this.makeColumnData(slaveOnlineColumnList, slaveDataMap);
                    this.saveTableData(slaveOnlineTable, slaveTableColumnDataList);
                }
            }
        } else {
            List<OnlineColumnData> slaveTableColumnDataList = this.makeColumnData(tableColumnList, slaveData);
            this.saveTableData(agileOnlineTable, slaveTableColumnDataList);
        }
        return true;
    }

    @Override
    public boolean updateTableData(String tableId, Map tableData) {
        AgileOnlineTable agileOnlineTable = this.agileOnlineTableService.getById(tableId);
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据从表不存在！");
        }
        List<AgileOnlineColumn> tableColumnList = this.getOnlineColumnList(agileOnlineTable);
        List<OnlineColumnData> tableColumnDataList = this.makeColumnData(tableColumnList, tableData);
        List<OnlineColumnData> updateColumnList = new LinkedList<>();
        List<OnlineColumnData> whereColumnList = new LinkedList<>();
        for (OnlineColumnData columnData : tableColumnDataList) {
            if (SysYesNo.YES.equals(columnData.getPrimaryFlag())) {
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

    @Override
    public boolean deleteTableData(String tableId, String dataId) {
        AgileOnlineTable agileOnlineTable = agileOnlineTableService.getById(tableId);
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据表已不存在！");
        }
        if (OnlineTableType.MASTER.equals(agileOnlineTable.getTableType())) { // 删除从表数据
            List<AgileOnlineTable> onlineRelationTableList = getOnlineRelationTable(agileOnlineTable.getFormId(), agileOnlineTable.getId());
            if (AgileStringUtil.isNotEmpty(onlineRelationTableList)) {
                Map masterTableData = getOneData(agileOnlineTable, dataId);
                if (AgileStringUtil.isNotEmpty(masterTableData)) {
                    for (AgileOnlineTable subOnlineTable : onlineRelationTableList) {
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
        List<OnlineFieldFilter> fieldFilterList = new ArrayList<>();
        fieldFilterList.add(this.makeTableFieldFilter(agileOnlineTable.getTableName(), agileOnlineTable.getPrimaryColumnName(), dataId));
        return agileOnlineOperationMapper.getOneData(agileOnlineTable.getTableName(), selectFields, null, fieldFilterList);
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

    /**
     * 查询关联从表
     *
     * @param formId  表单ID
     * @param tableId 主表ID
     * @return
     */
    private List<AgileOnlineTable> getOnlineRelationTable(String formId, String tableId) {
        LambdaQueryWrapper<AgileOnlineTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineTable::getFormId, formId);
        lambdaQueryWrapper.eq(AgileOnlineTable::getMasterTableId, tableId);
        return this.agileOnlineTableService.list(lambdaQueryWrapper);
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
        if (!ignoreParentId && SysYesNo.YES.equals(agileOnlineDict.getTreeFlag())) {
            sb.append(", ").append(agileOnlineDict.getParentKeyColumnName()).append(" parentId");
        }
        return sb.toString();
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
     * 拼装数据表查询字段
     *
     * @param agileOnlineTable
     * @return
     */
    private String makeTableSelectField(AgileOnlineTable agileOnlineTable) {
        StringBuilder selectFieldBuilder = new StringBuilder();
        getOnlineColumnList(agileOnlineTable).forEach(agileOnlineColumn -> {
            selectFieldBuilder
                    .append(agileOnlineTable.getTableName()).append(".")
                    .append(agileOnlineColumn.getColumnName()).append(' ');
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
            if (SysYesNo.NO.equals(onlineColumnData.getColumnNullable()) && AgileStringUtil.isEmpty(onlineColumnData.getColumnValue())) {
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
        if (SysYesNo.YES.equals(onlineColumnData.getPrimaryFlag())) {
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
                    onlineColumnData.setColumnValue(SysYesNo.NO);
                    break;
                default:
                    break;
            }
        }
    }

}
