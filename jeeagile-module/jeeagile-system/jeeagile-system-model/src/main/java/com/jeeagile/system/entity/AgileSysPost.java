package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
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
public class AgileSysPost extends AgileBaseModel<AgileSysPost> {
   /**
     * 岗位编码
     */
    @NotEmpty(message = "岗位编码不能为空！")
    @Size(max = 20, message = "岗位编码长度最大值为20！")
    private String postCode;

    /**
     * 岗位名称
     */
    @NotEmpty(message = "岗位名称不能为空！")
    @Size(max = 50, message = "岗位名称长度最大值为50！")
    private String postName;

    /**
     * 岗位排序
     */
    @NotNull(message = "岗位排序不能为空！")
    private Integer postSort;

    /**
     * 岗位状态（0:正常 1:停用）
     */
    @Pattern(regexp = "[01]", message = "岗位状态值必须为0或1或2（0：正常 1：停用）！")
    private String postStatus;

    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;

}
