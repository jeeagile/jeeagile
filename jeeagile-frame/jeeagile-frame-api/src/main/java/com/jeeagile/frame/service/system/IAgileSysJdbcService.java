package com.jeeagile.frame.service.system;

import com.jeeagile.frame.entity.system.AgileSysJdbc;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.vo.system.AgileJdbcTable;
import com.jeeagile.frame.vo.system.AgileJdbcTableColumn;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2023-07-17
 * @description 数据源管理 接口类
 */
public interface IAgileSysJdbcService extends IAgileBaseService<AgileSysJdbc> {
    /**
     * 获取数据源下数据表
     *
     * @return
     */
    List<AgileJdbcTable> selectTableList();

    /**
     * 获取数据源下数据表字段信息
     *
     * @param tableName 表名称
     * @return
     */
    List<AgileJdbcTableColumn> selectTableColumnList(String tableName);

    /**
     * 获取数据源下数据表字段信息
     *
     * @param tableName  表名称
     * @param columnName 字段名称
     * @return
     */
    AgileJdbcTableColumn selectTableColumn(String tableName, String columnName);
}
