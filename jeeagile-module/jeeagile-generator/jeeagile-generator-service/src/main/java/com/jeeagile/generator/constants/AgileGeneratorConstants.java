package com.jeeagile.generator.constants;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
public class AgileGeneratorConstants {

    private AgileGeneratorConstants() {
    }

    /**
     * 数据库字符串类型
     */
    public static final String[] DB_COLUMN_TYPE_STR = {"char", "varchar", "nvarchar", "varchar2", "tinytext", "text", "mediumtext", "longtext"};

    /**
     * 数据库时间类型
     */
    @SuppressWarnings("all")
    public static final String[] DB_COLUMN_TYPE_TIME = {"datetime", "time", "date", "timestamp"};

    /**
     * 数据库数字类型
     */
    @SuppressWarnings("all")
    public static final String[] DB_COLUMN_TYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer", "bit", "bigint", "float", "float", "double", "decimal"};

    /**
     * 字符串类型
     */
    public static final String JAVA_TYPE_STRING = "String";

    /**
     * 整型
     */
    public static final String JAVA_TYPE_INTEGER = "Integer";

    /**
     * 长整型
     */
    public static final String JAVA_TYPE_LONG = "Long";

    /**
     * 浮点型
     */
    public static final String JAVA_TYPE_DOUBLE = "Double";

    /**
     * 高精度计算类型
     */
    public static final String JAVA_TYPE_BIGDECIMAL = "BigDecimal";
    /**
     * 时间类型
     */
    public static final String JAVA_TYPE_DATE = "Date";

    /**
     * 文本框
     */
    public static final String HTML_TYPE_INPUT = "input";


    /**
     * 文本域
     */
    public static final String HTML_TYPE_TEXTAREA = "textarea";

    /**
     * 数字框
     */
    public static final String HTML_TYPE_NUMBER = "number";

    /**
     * 下拉框
     */
    public static final String HTML_TYPE_SELECT = "select";

    /**
     * 单选框
     */
    public static final String HTML_TYPE_RADIO = "radio";

    /**
     * 复选框
     */
    public static final String HTML_TYPE_CHECKBOX = "checkbox";

    /**
     * 日期控件
     */
    public static final String HTML_TYPE_DATETIME = "datetime";

    /**
     * 富文本控件
     */
    public static final String HTML_TYPE_EDITOR = "editor";

    @SuppressWarnings("all")
    public static final String[] BASE_COLUMN_NAME = {"id", "parent_id", "create_user", "create_time", "update_user", "update_time"};


    /**
     * 页面不需要编辑字段
     */
    @SuppressWarnings("all")
    public static final String[] COLUMN_NAME_NOT_EDIT = {"id", "create_user", "create_time", "update_user", "update_time"};

    /**
     * 页面不需要显示的列表字段
     */
    @SuppressWarnings("all")
    public static final String[] COLUMN_NAME_NOT_LIST = {"id", "create_user", "create_time", "update_user", "update_time", "remark"};

    /**
     * 页面不需要查询字段
     */
    @SuppressWarnings("all")
    public static final String[] COLUMN_NAME_NOT_QUERY = {"id", "parent_id", "create_user", "create_time", "update_user", "update_time", "remark"};

    /**
     * 模糊查询
     */
    public static final String QUERY_TYPE_LIKE = "LIKE";

    /**
     * 表类型 单表
     */
    public static final String TABLE_TYPE_CRUD = "crud";
    /**
     * 表类型 树表
     */
    public static final String TABLE_TYPE_TREE = "tree";
}
