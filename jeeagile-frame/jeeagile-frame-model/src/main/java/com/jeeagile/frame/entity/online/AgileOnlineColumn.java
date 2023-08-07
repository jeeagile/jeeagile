package com.jeeagile.frame.entity.online;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2023-07-27
 * @description 在线表单 数据表字段
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileOnlineColumn extends AgileBaseTenantModel<AgileOnlineColumn> {
    /**
     * 在线表单主键ID
     */
    @ApiModelProperty("在线表单主键ID")
    @NotNull(message = "表单ID不能为空！")
    private String formId;
    /**
     * 在线数据表主键ID
     */
    @ApiModelProperty("在线数据表主键ID")
    @NotNull(message = "数据表ID不能为空！")
    private String tableId;
    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String columnName;
    /**
     * 字段描述
     */
    @ApiModelProperty(value = "字段描述")
    private String columnComment;
    /**
     * 字段排序
     */
    @ApiModelProperty(value = "字段排序")
    private int columnSort;
    /**
     * 字段必填标识（0:否 1:是）
     */
    @ApiModelProperty(value = "是否可空")
    private String columnNullable;
    /**
     * 字段长度
     */
    @ApiModelProperty(value = "字段长度")
    private Long columnLength;
    /**
     * 字段精度
     */
    @ApiModelProperty(value = "字段精度")
    private String columnPrecision;
    /**
     * 字段默认值
     */
    @ApiModelProperty(value = "字段默认值")
    private String columnDefault;
    /**
     * 字段扩展
     */
    @ApiModelProperty(value = "字段扩展")
    private String columnExtra;
    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private String columnType;
    /**
     * 数据类型
     */
    @ApiModelProperty(value = "数据类型")
    private String dataType;
    /**
     * 主键标识
     */
    @ApiModelProperty(value = "主键标识")
    private String primaryFlag;
    /**
     * 主键类型(01：数据库自增  99：自定义)
     */
    @ApiModelProperty(value = "主键类型")
    private String primaryType;

    /**
     * 数据对象字段名称
     */
    @ApiModelProperty(value = "数据对象字段名称")
    private String fieldName;
    /**
     * 数据对象数据类型
     */
    @ApiModelProperty(value = "数据对象数据类型")
    private String fieldType;
    /**
     * 数据对象显示标签
     */
    @ApiModelProperty(value = "数据对象显示标签")
    private String fieldLabel;
    /**
     * 数据对象过滤类型 01:无过滤 02:普通过滤 03:范围过滤 04:模糊过滤
     */
    @ApiModelProperty(value = "数据对象过滤类型")
    private String filterType;
    /**
     * 数据对象字段分类
     */
    @ApiModelProperty(value = "数据对象字段分类")
    private String fieldKind;
    /**
     * 数据对象字典ID
     */
    @ApiModelProperty(value = "数据对象字典")
    private String dictId;

    public AgileOnlineColumn() {
    }

    @Builder
    public AgileOnlineColumn(String formId, String tableId) {
        this.formId = formId;
        this.tableId = tableId;
    }
}
