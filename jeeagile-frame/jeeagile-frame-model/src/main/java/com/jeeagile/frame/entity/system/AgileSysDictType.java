package com.jeeagile.frame.entity.system;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@HeadRowHeight(value = 20)
@ContentRowHeight(value = 18)
public class AgileSysDictType extends AgileBaseTenantModel<AgileSysDictType> {
    /**
     * 字典类型
     */
    @ExcelProperty(value = {"字典类型", "字典类型"}, index = 0)
    @ApiModelProperty("字典类型")
    @NotEmpty(message = "字典类型不能为空！")
    @Size(max = 50, message = "字典类型长度最大值为50！")
    private String dictType;

    /**
     * 字典名称
     */
    @ExcelProperty(value = {"字典类型", "字典名称"}, index = 1)
    @ApiModelProperty("字典名称")
    @NotEmpty(message = "字典名称不能为空！")
    @Size(max = 50, message = "字典名称长度最大值为50！")
    private String dictName;

    /**
     * 字典状态（0：正常 2：停用）
     */
    @ExcelProperty(value = {"字典类型", "字典状态"}, index = 2)
    @ApiModelProperty("字典状态")
    @Pattern(regexp = "[01]", message = "字典状态值必须为0或1或2（0：正常 1：停用）！")
    private String dictStatus;

    /**
     * 系统内置标识(1:是 0:否)
     */
    @ExcelProperty(value = {"字典类型", "系统内置标识"}, index = 3)
    @ApiModelProperty("系统内置标识")
    @Pattern(regexp = "[01]", message = "系统内置标识值必须为0或1（1:是 0:否）！")
    private String systemFlag;
    /**
     * 备注信息
     */
    @ExcelIgnore
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;

    public AgileSysDictType() {
    }

    @Builder
    public AgileSysDictType(String id, String dictType, String dictName, String dictStatus, String systemFlag) {
        this.id = id;
        this.dictType = dictType;
        this.dictName = dictName;
        this.dictStatus = dictStatus;
        this.systemFlag = systemFlag;
    }
}
