package com.jeeagile.generator.mapper;

import com.jeeagile.frame.annotation.AgileMapperScan;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.generator.entity.AgileGeneratorTable;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@AgileMapperScan
public interface AgileGeneratorTableMapper extends AgileBaseMapper<AgileGeneratorTable> {
    /**
     * 分页查询当前用户下所有表信息
     */
    AgilePage<AgileGeneratorTable> selectDbTablePage(AgilePage<AgileGeneratorTable> agilePage, @Param("agileGeneratorTable") AgileGeneratorTable agileGeneratorTable);

    /**
     * 根据表名获取表信息
     */
    AgileGeneratorTable selectDbTableByTableName(@Param("tableName") String tableName);

    /**
     * 根据表名获取表字段信息
     */
    List<AgileGeneratorTableColumn> selectDbTableColumnByTableName(@Param("tableName") String tableName);

}
