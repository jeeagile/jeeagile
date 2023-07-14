package com.jeeagile.frame.entity.system;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@HeadRowHeight(value = 20)
@ContentRowHeight(value = 18)
public class AgileSysPost extends AgileBaseTenantModel<AgileSysPost> {
    /**
     * 岗位编码
     */
    @ExcelProperty(value = {"岗位信息", "岗位编码"}, index = 0)
    @ApiModelProperty("岗位编码")
    @NotEmpty(message = "岗位编码不能为空！")
    @Size(max = 20, message = "岗位编码最大长度为20！")
    private String postCode;

    /**
     * 岗位名称
     */
    @ExcelProperty(value = {"岗位信息", "岗位名称"}, index = 1)
    @ApiModelProperty("岗位名称")
    @NotEmpty(message = "岗位名称不能为空！")
    @Size(max = 50, message = "岗位名称最大长度为50！")
    private String postName;

    /**
     * 岗位排序
     */
    @ExcelProperty(value = {"岗位信息", "岗位排序"}, index = 2)
    @ApiModelProperty("岗位排序")
    @NotNull(message = "岗位排序不能为空！")
    private Integer postSort;

    /**
     * 岗位状态（0:正常 1:停用）
     */
    @ExcelProperty(value = {"岗位信息", "岗位状态"}, index = 3)
    @ApiModelProperty("岗位状态")
    @Pattern(regexp = "[01]", message = "岗位状态值必须为0或1或2（0：正常 1：停用）！")
    private String postStatus;

    /**
     * 备注信息
     */
    @ExcelIgnore
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注最大长度为150！")
    private String remark;

}
