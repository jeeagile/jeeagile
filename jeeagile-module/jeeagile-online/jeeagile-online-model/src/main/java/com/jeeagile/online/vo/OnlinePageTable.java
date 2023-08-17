package com.jeeagile.online.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlinePageTable {
    /**
     * 数据表ID
     */
    @ApiModelProperty("数据表ID")
    private String tableId;
    /**
     * 在线表单主键ID
     */
    @ApiModelProperty("在线表单主键ID")
    private String formId;
    /**
     * 数据表名称
     */
    @ApiModelProperty("数据表名称")
    private String tableName;
    /**
     * 数据表描述
     */
    @ApiModelProperty("数据表描述")
    private String tableLabel;
    /**
     * 数据表类型（01:数据主表 02:一对一从表 03:一对多从表）
     */
    @ApiModelProperty("数据表类型")
    private String tableType;
    /**
     * 数据模型名称
     */
    @ApiModelProperty("数据模型名称")
    private String modelName;
    /**
     * 表主键字段ID
     */
    @ApiModelProperty("表主键字段ID")
    private String primaryColumnId;
    /**
     * 表主键字段名称
     */
    @ApiModelProperty("表主键字段名称")
    private String primaryColumnName;
    /**
     * 主表ID
     */
    @ApiModelProperty("主表ID")
    private String masterTableId;
    /**
     * 主表字段ID
     */
    @ApiModelProperty("主表字段ID")
    private String masterColumnId;
    /**
     * 主表字段名称
     */
    @ApiModelProperty("主表字段名称")
    private String masterColumnName;
    /**
     * 从表字段ID
     */
    @ApiModelProperty("从表字段ID")
    private String slaveColumnId;
    /**
     * 从表字段名称
     */
    @ApiModelProperty("从表字段名称")
    private String slaveColumnName;
    /**
     * 数据表字段
     */
    @ApiModelProperty("从表字段名称")
    List<OnlineTableColumn> tableColumnList;
}
