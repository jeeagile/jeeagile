package com.jeeagile.frame.entity.system;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.entity.AgileTenantModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileSysLogin extends AgileTenantModel<AgileSysLogin> {

    /**
     * 主键ID
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 登录类型
     */
    @ExcelProperty(value = {"登录日志", "登录类型"}, index = 0)
    @ApiModelProperty("登录类型")
    private String loginModule;

    /**
     * 登录用户名
     */
    @ExcelProperty(value = {"登录日志", "登录名称"}, index = 1)
    @ApiModelProperty("登录名称")
    private String loginName;

    /**
     * 登录时间
     */
    @ExcelProperty(value = {"登录日志", "登录时间"}, index = 2)
    @ApiModelProperty("登录时间")
    private Date loginTime;

    /**
     * 登录IP地址
     */
    @ExcelProperty(value = {"登录日志", "登录IP"}, index = 3)
    @ApiModelProperty("登录IP")
    private String loginIp;

    /**
     * 登录地址
     */
    @ExcelProperty(value = {"登录日志", "登录地址"}, index = 4)
    @ApiModelProperty("登录地址")
    private String loginAddress;

    /**
     * 登录设备名称
     */
    @ExcelProperty(value = {"登录日志", "登录设备名称"}, index = 5)
    @ApiModelProperty("登录设备名称")
    private String loginDevice;

    /**
     * 登录设备操作系统名称
     */
    @ExcelProperty(value = {"登录日志", "登录设备操作系统名称"}, index = 6)
    @ApiModelProperty("登录设备操作系统名称")
    private String loginOs;

    /**
     * 登录浏览器名称
     */
    @ExcelProperty(value = {"登录日志", "登录浏览器名称"}, index = 7)
    @ApiModelProperty("登录浏览器名称")
    private String loginBrowser;

    /**
     * 登录服务器IP地址
     */
    @ExcelProperty(value = {"登录日志", "登录服务器IP地址"}, index = 8)
    @ApiModelProperty("登录服务器IP")
    private String serverAddress;
    /**
     * 登录状态
     */
    @ExcelProperty(value = {"登录日志", "登录状态"}, index = 9)
    @ApiModelProperty("登录状态")
    private String status;

    /**
     * 登录信息
     */
    @ExcelProperty(value = {"登录日志", "登录信息"}, index = 10)
    @ApiModelProperty("登录信息")
    private String message;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
