package com.jeeagile.frame.vo.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineFieldFilter {

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
     * 过滤字段ID
     */
    @ApiModelProperty(value = "过滤字段ID")
    private String columnId;

    /**
     * 过滤字段名
     */
    @ApiModelProperty(value = "过滤字段名")
    private String columnName;

    /**
     * 过滤值
     */
    @ApiModelProperty(value = "过滤值")
    private Object columnValue;

    /**
     * 范围比较的最小值
     */
    @ApiModelProperty(value = "范围比较的最小值")
    private Object columnValueStart;

    /**
     * 范围比较的最大值
     */
    @ApiModelProperty(value = "范围比较的最大值")
    private Object columnValueEnd;

    /**
     * 仅当操作符为IN的时候使用
     */
    @ApiModelProperty(value = "仅当操作符为IN的时候使用")
    private Set<Object> columnValueList;

    /**
     * 过滤类型
     * 01:无过滤 02:普通过滤 03:范围过滤 04:模糊过滤 05：IN LIST列表过滤
     */
    @ApiModelProperty(value = "过滤类型")
    private String filterType = "02";
}
