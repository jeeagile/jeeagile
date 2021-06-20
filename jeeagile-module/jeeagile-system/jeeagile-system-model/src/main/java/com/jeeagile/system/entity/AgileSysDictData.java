package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileBaseTreeModel;
import jakarta.validation.constraints.NotNull;
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
public class AgileSysDictData extends AgileBaseTreeModel<AgileSysDictData> {
    /**
     * 字典类型
     */
    @NotNull(message = "字典类型不能为空！")
    @Size(max = 50, message = "字典类型长度最大值为50！")
    private String dictType;

    /**
     * 字典标签
     */
    @NotNull(message = "字典标签不能为空！")
    @Size(max = 50, message = "字典标签长度最大值为50！")
    private String dictLabel;

    /**
     * 字典键值
     */
    @NotNull(message = "字典键值不能为空！")
    @Size(max = 50, message = "字典键值长度最大值为50！")
    private String dictValue;

    /**
     * 字典状态（0:正常 1:停用）
     */
    @Pattern(regexp = "[01]", message = "字典状态值必须为0或2（0：正常 1：停用）！")
    private String dictStatus;
    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空！")
    private Integer dictSort;

    /**
     * 系统参数标识(1:是 0:否)
     */
    @Pattern(regexp = "[01]", message = "系统内置标识值必须为0或1（1:是 0:否）！")
    private String systemFlag;
    /**
     * 备注
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;
}
