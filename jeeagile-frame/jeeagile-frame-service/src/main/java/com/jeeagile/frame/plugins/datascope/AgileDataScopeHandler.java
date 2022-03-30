package com.jeeagile.frame.plugins.datascope;

import net.sf.jsqlparser.expression.Expression;

/**
 * @author JeeAgile
 * @date 2022-03-21
 * @description 数据权限处理器
 */
public interface AgileDataScopeHandler {

    /**
     * 获取数据权限 SQL 片段
     *
     * @param where             待执行 SQL Where 条件表达式
     * @param mappedStatementId Mybatis MappedStatement Id 根据该参数可以判断具体执行方法
     * @return JSqlParser 条件表达式
     */
    Expression getSqlSegment(Expression where, String mappedStatementId, Object parameter);
}
