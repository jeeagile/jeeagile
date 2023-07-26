package com.jeeagile.frame.entity.online;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2023-07-25
 * @description 在线表单 数据模型
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileOnlineTable extends AgileBaseTenantModel<AgileOnlineTable> {
    /**
     * 在线表单主键ID
     */
    @NotNull(message = "表单ID不能为空！")
    private String formId;
    /**
     * 数据表名称
     */
    @NotNull(message = "数据表名称不能为空！")
    private String tableName;
    /**
     * 数据表描述
     */
    @NotNull(message = "数据表描述不能为空！")
    private String tableLabel;
    /**
     * 数据表类型（01:数据主表 02:一对一从表 03:一对多从表）
     */
    @NotNull(message = "数据表类型不能为空！")
    private String tableType;
    /**
     * 数据模型名称
     */
    @NotNull(message = "数据模型名称不能为空！")
    private String modelName;
    /**
     * 主表字段ID
     */
    private String masterColumnId;
    /**
     * 主表字段名称
     */
    private String masterColumnName;
    /**
     * 从表字段ID
     */
    private String slaveColumnId;
    /**
     * 从表字段称
     */
    private String slaveColumnName;
}
