package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class AgileSysConfig extends AgileBaseModel<AgileSysConfig> {
    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称", position = 1)
    @NotEmpty(message = "参数名称不能为空！")
    @Size(max = 50, message = "参数名称长度最大值为50！")
    private String configName;

    /**
     * 参数键名
     */
    @ApiModelProperty(value = "参数键名", position = 2)
    @NotEmpty(message = "参数键名不能为空！")
    @Size(max = 50, message = "参数键名长度最大值为50！")
    private String configKey;

    /**
     * 参数键值
     */
    @ApiModelProperty(value = "参数键值", position = 3)
    @NotEmpty(message = "参数键值不能为空！")
    @Size(max = 150, message = "参数键值长度最大值为150！")
    private String configValue;

    /**
     * 系统参数标识(1:是 0:否)
     */
    @ApiModelProperty(value = "系统参数标识", position = 4)
    @Pattern(regexp = "[01]", message = "系统内置标识值必须为0或1（1:是 0:否）！")
    private String systemFlag;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注", position = 5)
    @Size(max = 150, message = "备注信息长度最大值为150！")
    private String remark;

}
