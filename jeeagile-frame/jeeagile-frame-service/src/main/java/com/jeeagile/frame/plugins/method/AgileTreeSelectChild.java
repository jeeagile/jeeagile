package com.jeeagile.frame.plugins.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

/**
 * @author JeeAgile
 * @date 2022-03-31
 * @description 自定义全局方法
 */
public class AgileTreeSelectChild extends AbstractMethod {

    private static String METHOD_NAME = "selectChild";

    private static String SELECT_CHILD_SQL = "SELECT %s FROM %s WHERE parent_id=#{parent_id}";

    public AgileTreeSelectChild() {
        super(METHOD_NAME);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format(SELECT_CHILD_SQL, this.sqlSelectColumns(tableInfo, false), tableInfo.getTableName());
        SqlSource sqlSource = new RawSqlSource(this.configuration, sql, mapperClass);
        return this.addSelectMappedStatementForTable(mapperClass, METHOD_NAME, sqlSource, tableInfo);
    }
}
