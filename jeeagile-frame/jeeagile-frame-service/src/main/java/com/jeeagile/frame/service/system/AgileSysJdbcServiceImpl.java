package com.jeeagile.frame.service.system;

import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.entity.system.AgileSysJdbc;
import com.jeeagile.frame.mapper.system.AgileSysJdbcMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.vo.system.AgileJdbcTable;
import com.jeeagile.frame.vo.system.AgileJdbcTableColumn;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2027-07-17
 * @description 数据源管理 接口实现类
 */
@AgileService
public class AgileSysJdbcServiceImpl extends AgileBaseServiceImpl<AgileSysJdbcMapper, AgileSysJdbc> implements IAgileSysJdbcService {
    @Override
    public List<AgileJdbcTable> selectTableList() {
        return this.baseMapper.getTableList();
    }

    @Override
    public List<AgileJdbcTableColumn> selectTableColumnList(String tableName) {
        List<AgileJdbcTableColumn> agileJdbcTableColumnList = this.baseMapper.getTableColumnList(tableName);
        agileJdbcTableColumnList.forEach(agileJdbcTableColumn -> {
            agileJdbcTableColumn.setColumnComment(this.handleColumnComment(agileJdbcTableColumn.getColumnComment()));
            agileJdbcTableColumn.setJavaType(this.convertToJavaType(agileJdbcTableColumn.getDataType()));
        });
        return agileJdbcTableColumnList;
    }

    /**
     * 处理字段描述
     *
     * @param columnComment
     * @return
     */
    private String handleColumnComment(String columnComment) {
        if (StringUtils.indexOf(columnComment, "（") > 0) {
            columnComment = StringUtils.substringBefore(columnComment, "（");
        }
        if (StringUtils.indexOf(columnComment, "(") > 0) {
            columnComment = StringUtils.substringBefore(columnComment, "(");
        }
        return columnComment;
    }

    /**
     * 转换数据对象java对象类型
     *
     * @param dataType 数据库字段数据类型
     * @return
     */
    private String convertToJavaType(String dataType) throws RuntimeException {
        if ("varchar".equals(dataType)
                || "char".equals(dataType)
                || "text".equals(dataType)
                || "longtext".equals(dataType)
                || "mediumtext".equals(dataType)
                || "tinytext".equals(dataType)) {
            return "String";
        }
        if ("int".equals(dataType)
                || "mediumint".equals(dataType)
                || "smallint".equals(dataType)
                || "tinyint".equals(dataType)) {
            return "Integer";
        }
        if ("bit".equals(dataType)) {
            return "Boolean";
        }
        if ("bigint".equals(dataType)) {
            return "Long";
        }
        if ("decimal".equals(dataType)) {
            return "BigDecimal";
        }
        if ("float".equals(dataType)
                || "double".equals(dataType)) {
            return "Double";
        }
        if ("date".equals(dataType)
                || "datetime".equals(dataType)
                || "timestamp".equals(dataType)
                || "time".equals(dataType)) {
            return "Date";
        }
        if ("blob".equals(dataType)) {
            return "byte[]";
        }
        throw new RuntimeException("Unsupported Column Type");
    }
}
