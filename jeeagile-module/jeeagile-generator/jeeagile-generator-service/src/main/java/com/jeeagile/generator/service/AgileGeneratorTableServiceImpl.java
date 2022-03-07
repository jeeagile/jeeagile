package com.jeeagile.generator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.generator.entity.AgileGeneratorTable;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;
import com.jeeagile.generator.mapper.AgileGeneratorTableMapper;
import com.jeeagile.generator.util.AgileGeneratorUtil;
import com.jeeagile.generator.vo.AgileGeneratorTableInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@AgileService
public class AgileGeneratorTableServiceImpl extends AgileBaseServiceImpl<AgileGeneratorTableMapper, AgileGeneratorTable> implements IAgileGeneratorTableService {

    @Autowired
    private IAgileGeneratorTableColumnService agileGeneratorTableColumnService;

    @Override
    public AgilePage<AgileGeneratorTable> selectTablePage(AgilePageable<AgileGeneratorTable> agilePageable) {
        return this.page(agilePageable, getGeneratorTableQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public AgilePage<AgileGeneratorTable> selectDbTablePage(AgilePageable<AgileGeneratorTable> agilePageable) {
        AgilePage<AgileGeneratorTable> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return this.baseMapper.selectDbTablePage(agilePage, agilePageable.getQueryCond());
    }

    @Override
    public boolean importTable(List<String> tableNameList) {
        for (String tableName : tableNameList) {
            AgileGeneratorTable agileGeneratorTable = this.getBaseMapper().selectDbTableByTableName(tableName);
            if (agileGeneratorTable == null || AgileStringUtil.isEmpty(agileGeneratorTable.getTableName())) {
                throw new AgileValidateException("表(" + tableName + ")不存在！");
            }
            AgileGeneratorUtil.initGeneratorTable(agileGeneratorTable);
            this.save(agileGeneratorTable);
            List<AgileGeneratorTableColumn> agileGeneratorTableColumnList = this.getBaseMapper().selectDbTableColumnByTableName(tableName);
            if (agileGeneratorTableColumnList != null && !agileGeneratorTableColumnList.isEmpty()) {
                agileGeneratorTableColumnList.forEach(agileGeneratorTableColumn ->
                        AgileGeneratorUtil.initColumnField(agileGeneratorTable, agileGeneratorTableColumn)
                );
            }
            agileGeneratorTableColumnService.saveBatch(agileGeneratorTableColumnList);
        }
        return true;
    }

    @Override
    public AgileGeneratorTableInfo selectTableInfoById(String agileGeneratorTableId) {
        AgileGeneratorTableInfo agileGeneratorTableInfo = new AgileGeneratorTableInfo();
        AgileGeneratorTable agileGeneratorTable = this.getById(agileGeneratorTableId);
        BeanUtils.copyProperties(agileGeneratorTable, agileGeneratorTableInfo);
        if (agileGeneratorTable != null && !AgileStringUtil.isEmpty(agileGeneratorTable.getId())) {
            agileGeneratorTableInfo.setAgileGeneratorTableColumnList(agileGeneratorTableColumnService.selectListByTableId(agileGeneratorTableId));
        }
        return agileGeneratorTableInfo;
    }

    @Override
    public boolean updateTableInfoById(AgileGeneratorTableInfo agileGeneratorTableInfo) {
        //更新主表信息
        boolean updateFlag = this.updateById(agileGeneratorTableInfo);
        if (updateFlag) {
            List<AgileGeneratorTableColumn> agileGeneratorTableColumnList = agileGeneratorTableInfo.getAgileGeneratorTableColumnList();
            if (agileGeneratorTableColumnList != null && !agileGeneratorTableColumnList.isEmpty()) {
                updateFlag = agileGeneratorTableColumnService.saveOrUpdateBatch(agileGeneratorTableColumnList);
            }
        }
        return updateFlag;
    }

    @Override
    public boolean deleteTableById(String agileGeneratorTableId) {
        agileGeneratorTableColumnService.deleteByTableId(agileGeneratorTableId);
        return this.removeById(agileGeneratorTableId);
    }

    @Override
    public boolean syncTableById(String agileGeneratorTableId) {
        AgileGeneratorTableInfo tableInfo = this.selectTableInfoById(agileGeneratorTableId);
        if (tableInfo != null) {
            List<AgileGeneratorTableColumn> tableColumnList = tableInfo.getAgileGeneratorTableColumnList();
            List<String> tableColumnNameList = tableColumnList.stream().map(AgileGeneratorTableColumn::getColumnName).collect(Collectors.toList());
            List<AgileGeneratorTableColumn> dbTableColumnList = this.getBaseMapper().selectDbTableColumnByTableName(tableInfo.getTableName());
            List<String> dbTableColumnNameList = dbTableColumnList.stream().map(AgileGeneratorTableColumn::getColumnName).collect(Collectors.toList());
            for (AgileGeneratorTableColumn agileGeneratorTableColumn : dbTableColumnList) {
                if (!tableColumnNameList.contains(agileGeneratorTableColumn.getColumnName())) {
                    AgileGeneratorUtil.initColumnField(tableInfo, agileGeneratorTableColumn);
                    agileGeneratorTableColumnService.save(agileGeneratorTableColumn);
                }
            }
            List<String> deleteColumnIdList = tableColumnList.stream().filter(genTableColumn -> !dbTableColumnNameList.contains(genTableColumn.getColumnName())).map(AgileGeneratorTableColumn::getId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(deleteColumnIdList)) {
                agileGeneratorTableColumnService.removeByIds(deleteColumnIdList);
            }
        } else {
            throw new AgileValidateException("表生成信息已不存在无法同步！");
        }
        return true;
    }

    @Override
    public Map<String, String> previewCode(String agileGeneratorTableId) {
        // 查询表信息
        AgileGeneratorTableInfo agileGeneratorTableInfo = this.selectTableInfoById(agileGeneratorTableId);
        if (agileGeneratorTableInfo != null && AgileStringUtil.isNotEmpty(agileGeneratorTableInfo.getId())) {
            return AgileGeneratorUtil.generatorCode(agileGeneratorTableInfo);
        } else {
            throw new AgileFrameException("生成表信息已不存在无法生成预览文件！");
        }
    }

    @Override
    public byte[] downloadCode(List<String> agileGeneratorTableIdList) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            for (String agileGeneratorTableId : agileGeneratorTableIdList) {
                AgileGeneratorTableInfo agileGeneratorTableInfo = this.selectTableInfoById(agileGeneratorTableId);
                if (agileGeneratorTableInfo != null && AgileStringUtil.isNotEmpty(agileGeneratorTableInfo.getId())) {
                    AgileGeneratorUtil.generatorCode(agileGeneratorTableInfo, zipOutputStream);
                }
            }
            IOUtils.close(zipOutputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new AgileFrameException("代码生成异常！");
        }
    }

    private QueryWrapper<AgileGeneratorTable> getGeneratorTableQueryWrapper(AgileGeneratorTable agileGeneratorTable) {
        QueryWrapper<AgileGeneratorTable> queryWrapper = new QueryWrapper<>();
        if (agileGeneratorTable != null) {
            if (AgileStringUtil.isNotEmpty(agileGeneratorTable.getTableName())) {
                queryWrapper.lambda().like(AgileGeneratorTable::getTableName, agileGeneratorTable.getTableName());
            }
            if (AgileStringUtil.isNotEmpty(agileGeneratorTable.getTableComment())) {
                queryWrapper.lambda().like(AgileGeneratorTable::getTableComment, agileGeneratorTable.getTableComment());
            }
        }
        queryWrapper.lambda().orderByDesc(AgileGeneratorTable::getCreateTime);
        return queryWrapper;
    }
}
