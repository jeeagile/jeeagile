package com.jeeagile.frame.plugins.parser;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

public abstract class AgileSqlParserSupport {
    public String parserSingle(String sql, Object obj, Object parameter) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            return processParser(statement, 0, sql, obj, parameter);
        } catch (JSQLParserException e) {
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e.getCause(), sql);
        }
    }

    public String parserMulti(String sql, Object obj, Object parameter) {
        try {
            StringBuilder sb = new StringBuilder();
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            int i = 0;
            for (Statement statement : statements.getStatements()) {
                if (i > 0) {
                    sb.append(StringPool.SEMICOLON);
                }
                sb.append(processParser(statement, i, sql, obj, parameter));
                i++;
            }
            return sb.toString();
        } catch (JSQLParserException e) {
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e.getCause(), sql);
        }
    }

    /**
     * 执行 SQL 解析
     */
    protected String processParser(Statement statement, int index, String sql, Object obj, Object parameter) {
        if (statement instanceof Select) {
            this.processSelect((Select) statement, index, sql, obj, parameter);
        } else if (statement instanceof Insert) {
            this.processInsert((Insert) statement, index, sql, obj, parameter);
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement, index, sql, obj, parameter);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement, index, sql, obj, parameter);
        }
        return statement.toString();
    }

    /**
     * 新增
     */
    protected void processInsert(Insert insert, int index, String sql, Object obj, Object parameter) {
        throw new UnsupportedOperationException();
    }

    /**
     * 删除
     */
    protected void processDelete(Delete delete, int index, String sql, Object obj, Object parameter) {
        throw new UnsupportedOperationException();
    }

    /**
     * 更新
     */
    protected void processUpdate(Update update, int index, String sql, Object obj, Object parameter) {
        throw new UnsupportedOperationException();
    }

    /**
     * 查询
     */
    protected void processSelect(Select select, int index, String sql, Object obj, Object parameter) {
        throw new UnsupportedOperationException();
    }
}
