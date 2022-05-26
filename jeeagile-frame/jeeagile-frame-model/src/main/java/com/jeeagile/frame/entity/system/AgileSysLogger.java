package com.jeeagile.frame.entity.system;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jeeagile.frame.entity.AgileBaseModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 用户操作日志 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileSysLogger extends AgileBaseModel<AgileSysLogger> {
    /**
     * 操作模块
     */
    @ExcelProperty(value = {"操作日志", "操作模块"}, index = 0)
    @ApiModelProperty("操作模块")
    @NotEmpty(message = "操作模块不能为空！")
    private String operateModule;

    /**
     * 操作详细描述
     */
    @ExcelProperty(value = {"操作日志", "操作描述"}, index = 1)
    @ApiModelProperty("操作详细描述")
    private String operateNotes;

    /**
     * 操作类型
     */
    @ExcelProperty(value = {"操作日志", "操作类型"}, index = 2)
    @ApiModelProperty("操作类型")
    @NotEmpty(message = "操作类型不能为空！")
    private String operateType;

    /**
     * 操作人员名称
     */
    @ExcelProperty(value = {"操作日志", "操作人员"}, index = 3)
    @ApiModelProperty("操作人员名称")
    @NotEmpty(message = "操作人员名称不能为空！")
    private String operateUser;

    /**
     * 请求URI
     */
    @ExcelProperty(value = {"操作日志", "请求URI"}, index = 4)
    @ApiModelProperty("请求URI")
    @NotEmpty(message = "请求URI不能为空！")
    private String requestUri;

    /**
     * 请求方式
     */
    @ExcelProperty(value = {"操作日志", "请求方式"}, index = 5)
    @ApiModelProperty("请求方式")
    @NotEmpty(message = "请求方式不能为空！")
    private String requestMethod;

    /**
     * 请求参数
     */
    @ExcelProperty(value = {"操作日志", "请求参数"}, index = 6)
    @ApiModelProperty("请求参数")
    private String requestParam;

    /**
     * 返回参数
     */
    @ExcelProperty(value = {"操作日志", "返回参数"}, index = 7)
    @ApiModelProperty("返回参数")
    private String responseParam;

    /**
     * 执行方法
     */
    @ExcelProperty(value = {"操作日志", "执行方法"}, index = 8)
    @ApiModelProperty("执行方法")
    private String executeMethod;

    /**
     * 操作ip
     */
    @ExcelProperty(value = {"操作日志", "操作IP"}, index = 9)
    @ApiModelProperty("操作IP")
    @NotEmpty(message = "操作ip不能为空！")
    private String operateIp;

    /**
     * 操作地址
     */
    @ExcelProperty(value = {"操作日志", "操作地址"}, index = 10)
    @ApiModelProperty("操作地址")
    private String operateAddress;

    /**
     * 操作设备名称
     */
    @ExcelProperty(value = {"操作日志", "操作设备名称"}, index = 11)
    @ApiModelProperty("操作设备名称")
    @NotEmpty(message = "操作设备名称不能为空！")
    private String operateDevice;

    /**
     * 操作设备操作系统名称
     */
    @ExcelProperty(value = {"操作日志", "操作设备操作系统名称"}, index = 12)
    @ApiModelProperty("操作设备操作系统名称")
    @NotEmpty(message = "操作设备操作系统名称不能为空！")
    private String operateOs;

    /**
     * 操作浏览器名称
     */
    @ExcelProperty(value = {"操作日志", "操作浏览器名称"}, index = 13)
    @ApiModelProperty("操作浏览器名称")
    @NotEmpty(message = "操作浏览器名称不能为空！")
    private String operateBrowser;

    /**
     * 服务器地址
     */
    @ExcelProperty(value = {"操作日志", "服务器地址"}, index = 14)
    @ApiModelProperty("服务器地址")
    @NotEmpty(message = "服务器地址不能为空！")
    private String serverAddress;

    /**
     * 执行时间(毫秒)
     */
    @ExcelProperty(value = {"操作日志", "执行时间(毫秒)"}, index = 15)
    @ApiModelProperty("执行时间(毫秒)")
    @NotNull(message = "执行时间(毫秒)不能为空！")
    private Long executeTime;

    /**
     * 操作状态（0：成功 1：失败）
     */
    @ExcelProperty(value = {"操作日志", "操作状态"}, index = 16)
    @ApiModelProperty("操作状态")
    @NotEmpty(message = "操作状态不能为空！")
    private String status;

    /**
     * 操作信息
     */
    @ExcelProperty(value = {"操作日志", "操作信息"}, index = 17)
    @ApiModelProperty("操作信息")
    private String message;
}
