package com.jeeagile.frame.entity.online;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 在线表单 字典管理
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileOnlineDict extends AgileBaseTenantModel<AgileOnlineDict> {
    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    @NotEmpty(message = "字典名称不能为空！")
    @Size(max = 50, message = "字典名称最大长度为50！")
    private String dictName;
    /**
     * 字典类型（01:数据表字典 02:系统管理字典 99:自定义字典）
     */
    @ApiModelProperty("字典类型")
    @NotEmpty(message = "字典类型不能为空！")
    private String dictType;
    /**
     * 系统字典类型
     */
    @ApiModelProperty("系统字典类型")
    @Size(max = 50, message = "系统字典类型最大长度为50！")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String systemDictType;
    /**
     * 字典表名称
     */
    @ApiModelProperty("字典表名称")
    @Size(max = 50, message = "表类型最大长度为50！")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String tableName;
    /**
     * 字典表树形标识
     */
    @ApiModelProperty("字典树形标识")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String treeFlag;
    /**
     * 字典表父字段名称
     */
    @ApiModelProperty("字典表父字段名称")
    @Size(max = 50, message = "字典表父字段名称最大长度为50！")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String parentKeyColumnName;
    /**
     * 字典表键字段名称
     */
    @ApiModelProperty("字典表键字段名称")
    @Size(max = 50, message = "字典表键字段名称最大长度为50！")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String keyColumnName;
    /**
     * 字典值字段名称
     */
    @ApiModelProperty("字典值字段名称")
    @Size(max = 50, message = "字典值字段名称最大长度为50！")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String valueColumnName;
    /**
     * 字典标签字段名称
     */
    @ApiModelProperty("字典标签字段名称")
    @Size(max = 50, message = "字典标签字段名称最大长度为50！")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String labelColumnName;
    /**
     * 字典数据JSON
     */
    @ApiModelProperty("字典数据JSON")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String dictDataJson;
    /**
     * 字段参数JSON
     */
    @ApiModelProperty("字段参数JSON")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String dictParamJson;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注信息最大长度为150！")
    private String remark;
}
