package com.jeeagile.frame.mapper.system;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.entity.system.AgileSysJdbc;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.frame.vo.system.AgileJdbcTable;
import com.jeeagile.frame.vo.system.AgileJdbcTableColumn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2023-07-17
 * @description 数据源管理
 */
@AgileMapper
public interface AgileSysJdbcMapper extends AgileBaseMapper<AgileSysJdbc> {
    /**
     * 获取数据库所有数据表
     *
     * @return
     */
    @Select("<script>"
            + " SELECT"
            + "     table_name tableName, table_comment tableComment"
            + " FROM"
            + "     information_schema.tables"
            + " WHERE"
            + "     table_schema = (SELECT DATABASE())"
            + "     AND UPPER(table_name) NOT LIKE 'ACT_%'"
            + "     AND UPPER(table_name) NOT LIKE 'QRTZ_%'"
            + "</script>")
    @InterceptorIgnore(tenantLine = "true")
    List<AgileJdbcTable> getTableList();

    /**
     * 获取表字段信息
     *
     * @param tableName 表名
     * @return
     */
    @Select("<script>"
            + "SELECT "
            + "    column_name column_name, "
            + "    column_comment column_comment, "
            + "    CASE WHEN column_key = 'PRI' THEN 1 ELSE 0 END AS primary_flag, "
            + "    data_type data_type, "
            + "    column_type column_type, "
            + "    ordinal_position column_sort, "
            + "    (case when is_nullable = 'no' then '0' else '1' end) as column_nullable, "
            + "    character_maximum_length column_length, "
            + "    numeric_precision column_precision, "
            + "    numeric_scale column_scale, "
            + "    column_default column_default, "
            + "    extra  column_extra "
            + "FROM "
            + "    information_schema.columns "
            + "WHERE "
            + "    table_name = #{tableName}"
            + "    AND table_schema = (SELECT DATABASE()) "
            + "ORDER BY ordinal_position "
            + "</script>")
    @InterceptorIgnore(tenantLine = "true")
    List<AgileJdbcTableColumn> getTableColumnList(@Param("tableName") String tableName);


    /**
     * 获取表字段信息
     *
     * @param tableName 表名
     * @return
     */
    @Select("<script>"
            + "SELECT "
            + "    column_name column_name, "
            + "    column_comment column_comment, "
            + "    CASE WHEN column_key = 'PRI' THEN 1 ELSE 0 END AS primary_flag, "
            + "    data_type data_type, "
            + "    column_type column_type, "
            + "    ordinal_position column_sort, "
            + "    (case when is_nullable = 'no' then '0' else '1' end) as column_nullable, "
            + "    character_maximum_length column_length, "
            + "    numeric_precision column_precision, "
            + "    numeric_scale column_scale, "
            + "    column_default column_default, "
            + "    extra  column_extra "
            + "FROM "
            + "    information_schema.columns "
            + "WHERE "
            + "    table_name = #{tableName}"
            + "    AND column_name = #{columnName}"
            + "    AND table_schema = (SELECT DATABASE()) "
            + "ORDER BY ordinal_position "
            + "</script>")
    @InterceptorIgnore(tenantLine = "true")
    AgileJdbcTableColumn getTableColumn(@Param("tableName") String tableName, @Param("columnName") String columnName);
}