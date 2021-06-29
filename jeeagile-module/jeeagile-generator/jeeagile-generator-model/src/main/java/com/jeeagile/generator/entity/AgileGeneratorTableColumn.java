package com.jeeagile.generator.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileGeneratorTableColumn extends AgileBaseModel<AgileGeneratorTableColumn> {
    /**
     * 归属表编号
     */
    private String tableId;
    /**
     * 列名称
     */
    private String columnName;
    /**
     * 列描述
     */
    private String columnComment;
    /**
     * 列类型
     */
    private String columnType;
    /**
     * 排序
     */
    private Integer columnSort;
    /**
     * JAVA类型
     */
    private String javaType;
    /**
     * JAVA字段名
     */
    private String javaField;
    /**
     * 是否主键（1是）
     */
    private String pkFlag;
    /**
     * 主键类型(1：数据库自增  2：UUID 3：自定义)
     */
    private String pkType;
    /**
     * 是否必填（1是）
     */
    private String requiredFlag;
    /**
     * 是否为插入字段（1是）
     */
    private String insertFlag;
    /**
     * 是否编辑字段（1是）
     */
    private String editFlag;
    /**
     * 是否列表字段（1是）
     */
    private String listFlag;
    /**
     * 是否查询字段（1是）
     */
    private String queryFlag;
    /**
     * '询方式（等于、不等于、大于、小于、范围）
     */
    private String queryType;
    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    private String htmlType;
    /**
     * 字典类型
     */
    private String dictType;
}
