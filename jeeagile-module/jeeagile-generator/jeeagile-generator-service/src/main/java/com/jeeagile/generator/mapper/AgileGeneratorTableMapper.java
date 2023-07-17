package com.jeeagile.generator.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.generator.entity.AgileGeneratorTable;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@AgileMapper
@InterceptorIgnore(tenantLine = "true")
public interface AgileGeneratorTableMapper extends AgileBaseMapper<AgileGeneratorTable> {
    /**
     * 分页查询当前用户下所有表信息
     */
    @InterceptorIgnore(tenantLine = "true")
    AgilePage<AgileGeneratorTable> selectDbTablePage(AgilePage<AgileGeneratorTable> agilePage, @Param("agileGeneratorTable") AgileGeneratorTable agileGeneratorTable);

    /**
     * 根据表名获取表信息
     */
    @InterceptorIgnore(tenantLine = "true")
    AgileGeneratorTable selectDbTableByTableName(@Param("tableName") String tableName);

    /**
     * 根据表名获取表字段信息
     */
    @InterceptorIgnore(tenantLine = "true")
    List<AgileGeneratorTableColumn> selectDbTableColumnByTableName(@Param("tableName") String tableName);

}
