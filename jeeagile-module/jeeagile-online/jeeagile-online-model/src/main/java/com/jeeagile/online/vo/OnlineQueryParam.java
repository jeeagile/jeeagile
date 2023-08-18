package com.jeeagile.online.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineQueryParam {
    /**
     * 字典数据id
     */
    @ApiModelProperty(value = "字典数据id")
    private String dictId;
    /**
     * 数据表ID
     */
    @ApiModelProperty(value = "数据表ID")
    private String tableId;
    /**
     * 过滤条件
     */
    @ApiModelProperty(value = "过滤条件")
    private List<OnlineFieldFilter> filterList;
    /**
     * 查询字段
     */
    @ApiModelProperty(value = "查询字段")
    private List<OnlineFieldQuery> queryList;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private List<OnlineFieldOrder> orderList;
}
