package com.jeeagile.frame.entity.system;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.core.util.AgileDateUtil;
import com.jeeagile.frame.entity.AgileBaseModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
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
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码")
    @NotEmpty(message = "租户编码不能为空！")
    @Size(max = 20, message = "租户编码最大长度为20！")
    private String tenantCode;
    /**
     * 租户名称
     */
    @ApiModelProperty(value = "租户名称")
    @NotEmpty(message = "租户名称不能为空！")
    @Size(max = 50, message = "租户名称最大长度为50！")
    private String tenantName;
    /**
     * 租户状态（0:启用 1:停用）
     */
    @ApiModelProperty(value = "租户启用状态")
    @Pattern(regexp = "[01]", message = "租户启用状态值必须为0或1（0：启用 1：停用）！")
    private String enableStatus;

    /**
     * 租户审核状态（0:审核通过 1:审核通过）
     */
    @ApiModelProperty(value = "租户审核状态")
    private String auditStatus;
    /**
     * 租户类型（0:本地 1:远程）
     * 本地：同库同Schema
     * 远程：同库不同Schema 或 完全独立数据库
     */
    @ApiModelProperty(value = "租户类型")
    private String tenantType;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "过期时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date expirationDate;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注")
    @Size(max = 150, message = "备注信息长度最大值为150！")
    private String remark;

}
