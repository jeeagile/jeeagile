package com.jeeagile.frame.entity.system;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.jeeagile.frame.entity.AgileBaseModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author JeeAgile
 * @date 2022-06-09
 * @description 用户分组
 */
@Data
@HeadRowHeight(value = 20)
@ContentRowHeight(value = 18)
public class AgileSysGroup extends AgileBaseModel<AgileSysGroup> {
    /**
     * 用户分组编码
     */
    @ExcelProperty(value = {"用户分组信息", "用户分组编码"}, index = 0)
    @ApiModelProperty("用户分组编码")
    @NotEmpty(message = "用户分组编码不能为空！")
    @Size(max = 20, message = "用户分组编码长度最大值为20！")
    private String groupCode;

    /**
     * 用户分组名称
     */
    @ExcelProperty(value = {"用户分组信息", "用户分组名称"}, index = 1)
    @ApiModelProperty("用户分组名称")
    @NotEmpty(message = "用户分组名称不能为空！")
    @Size(max = 50, message = "用户分组名称长度最大值为50！")
    private String groupName;

    /**
     * 用户分组排序
     */
    @ExcelProperty(value = {"用户分组信息", "用户分组排序"}, index = 2)
    @ApiModelProperty("用户分组排序")
    @NotNull(message = "用户分组排序不能为空！")
    private Integer groupSort;

    /**
     * 用户分组状态（0:正常 1:停用）
     */
    @ExcelProperty(value = {"用户分组信息", "用户分组状态"}, index = 3)
    @ApiModelProperty("用户分组状态")
    @Pattern(regexp = "[01]", message = "用户分组状态值必须为0或1或2（0：正常 1：停用）！")
    private String groupStatus;

    /**
     * 备注信息
     */
    @ExcelIgnore
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;

}
