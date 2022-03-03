package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileSysDictType extends AgileBaseModel<AgileSysDictType> {
    /**
     * 字典类型
     */
    @NotEmpty(message = "字典类型不能为空！")
    @Size(max = 50, message = "字典类型长度最大值为50！")
    private String dictType;

    /**
     * 字典名称
     */
    @NotEmpty(message = "字典名称不能为空！")
    @Size(max = 50, message = "字典名称长度最大值为50！")
    private String dictName;

    /**
     * 状态（0：正常 2：停用）
     */
    @Pattern(regexp = "[01]", message = "字典状态值必须为0或1或2（0：正常 1：停用）！")
    private String dictStatus;

    /**
     * 系统参数标识(1:是 0:否)
     */
    @Pattern(regexp = "[01]", message = "系统内置标识值必须为0或1（1:是 0:否）！")
    private String systemFlag;
    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;

}
