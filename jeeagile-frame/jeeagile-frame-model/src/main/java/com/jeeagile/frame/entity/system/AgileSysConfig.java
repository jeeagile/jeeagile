package com.jeeagile.frame.entity.system;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 参数配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileSysConfig extends AgileBaseTenantModel<AgileSysConfig> {
    /**
     * 参数名称
     */
    @ExcelProperty(value = {"参数配置", "参数名称"}, index = 0)
    @ApiModelProperty("参数名称")
    @NotEmpty(message = "参数名称不能为空！")
    @Size(max = 50, message = "参数名称最大长度为50！")
    private String configName;

    /**
     * 参数键名
     */
    @ExcelProperty(value = {"参数配置", "参数键名"}, index = 1)
    @ApiModelProperty("参数键名")
    @NotEmpty(message = "参数键名不能为空！")
    @Size(max = 50, message = "参数键名最大长度为50！")
    private String configKey;

    /**
     * 参数键值
     */
    @ExcelProperty(value = {"参数配置", "参数键值"}, index = 2)
    @ApiModelProperty("参数键值")
    @NotEmpty(message = "参数键值不能为空！")
    @Size(max = 150, message = "参数键值最大长度为150！")
    private String configValue;

    /**
     * 系统内置标识(1:是 0:否)
     */
    @ExcelProperty(value = {"参数配置", "系统内置标识"}, index = 3)
    @ApiModelProperty("系统内置标识")
    @Pattern(regexp = "[01]", message = "系统内置标识值必须为0或1（1:是 0:否）！")
    private String systemFlag;

    /**
     * 备注信息
     */
    @ExcelIgnore
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注信息最大长度为150！")
    private String remark;

    public AgileSysConfig() {
    }

    @Builder
    public AgileSysConfig(String id, String configKey, String configName, String configValue, String systemFlag) {
        this.id = id;
        this.configKey = configKey;
        this.configName = configName;
        this.configValue = configValue;
        this.systemFlag = systemFlag;
    }
}
