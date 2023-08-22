package com.jeeagile.frame.vo.system;

import com.jeeagile.frame.vo.AgileBaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2027-07-17
 * @description 数据源表字段信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileJdbcTableColumn extends AgileBaseVo {
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
    @ApiModelProperty(value = "字段必填标识（0:否 1:是）")
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
     * 数据范围 小数点后位数
     */
    @ApiModelProperty(value = "数据范围")
    private String columnScale;
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
     * JAVA类型
     */
    @ApiModelProperty(value = "JAVA类型")
    private String javaType;
}
