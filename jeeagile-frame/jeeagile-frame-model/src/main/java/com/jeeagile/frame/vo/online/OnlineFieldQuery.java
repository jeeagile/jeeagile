package com.jeeagile.frame.vo.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineFieldQuery {

    /**
     * 表ID
     */
    @ApiModelProperty(value = "表ID")
    private String tableId;
    /**
     * 表名
     */
    @ApiModelProperty(value = "表名")
    private String tableName;

    /**
     * 表模型名称
     */
    @ApiModelProperty(value = "表模型名称")
    private String modelName;

    /**
     * 表类型
     */
    @ApiModelProperty(value = "表类型")
    private String tableType;

    /**
     * 过滤字段ID
     */
    @ApiModelProperty(value = "过滤字段ID")
    private String columnId;

    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名")
    private String columnName;

    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名")
    private String fieldName;
}
