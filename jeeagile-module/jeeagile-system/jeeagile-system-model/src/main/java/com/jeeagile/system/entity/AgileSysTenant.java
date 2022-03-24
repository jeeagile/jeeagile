package com.jeeagile.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeeagile.frame.entity.AgileBaseModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2022-03-22
 * @description 租户管理
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileSysTenant extends AgileBaseModel<AgileSysTenant> {
    /**
     * 部门编码
     */
    @ApiModelProperty(value = "租户编码", position = 1)
    @NotEmpty(message = "租户编码不能为空！")
    @Size(max = 20, message = "租户编码最大长度为20！")
    private String tenantCode;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "租户名称", position = 2)
    @NotEmpty(message = "租户名称不能为空！")
    @Size(max = 50, message = "租户名称最大长度为50！")
    private String tenantName;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期", position = 3)
    @NotNull(message = "开始日期不能为空！")
    @JSONField(format = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期", position = 4)
    @NotNull(message = "结束日期不能为空！")
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 租户状态（0:正常 1:停用）
     */
    @ApiModelProperty(value = "租户状态", position = 5)
    @Pattern(regexp = "[01]", message = "租户状态值必须为0或1（0：正常 1：停用）！")
    private String tenantStatus;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注", position = 6)
    @Size(max = 150, message = "备注信息长度最大值为150！")
    private String remark;

    /**
     * 启用状态
     * -1 未知状态
     * 0 正常状态
     * 1 未到启用时间
     * 2 已到期
     *
     * @return
     */
    @ApiModelProperty(value = "租户启用状态", name = "enableStatus", position = 6)
    @JSONField(name = "enableStatus")
    public String getEnableStatus() {
        if (this.startDate != null && this.endDate != null) {
            Date currentDate = new Date();
            if (currentDate.after(startDate) && currentDate.before(this.endDate)) {
                return "0";
            } else if (currentDate.before(startDate)) {
                return "1";
            } else if (currentDate.after(this.endDate)) {
                return "2";
            } else {
                return "-1";
            }
        }
        return "-1";
    }
}
