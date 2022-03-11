package com.jeeagile.logger.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
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
public class AgileLoggerOperate extends AgileBaseModel<AgileLoggerOperate> {
    /**
     * 操作模块
     */
    @NotEmpty(message = "操作模块不能为空！")
    private String operateModule;

    /**
     * 操作详细描述
     */
    private String operateNotes;

    /**
     * 操作类型
     */
    @NotEmpty(message = "操作类型不能为空！")
    private String operateType;

    /**
     * 操作人员名称
     */
    @NotEmpty(message = "操作人员名称不能为空！")
    private String operateUser;

    /**
     * 请求uri
     */
    @NotEmpty(message = "请求uri不能为空！")
    private String requestUri;

    /**
     * 请求方式
     */
    @NotEmpty(message = "请求方式不能为空！")
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 返回参数
     */
    private String responseParam;

    /**
     * 执行方法
     */
    private String executeMethod;

    /**
     * 操作ip
     */
    @NotEmpty(message = "操作ip不能为空！")
    private String operateIp;

    /**
     * 操作地址
     */
    private String operateAddress;

    /**
     * 操作设备名称
     */
    @NotEmpty(message = "操作设备名称不能为空！")
    private String operateDevice;

    /**
     * 操作设备操作系统名称
     */
    @NotEmpty(message = "操作设备操作系统名称不能为空！")
    private String operateOs;

    /**
     * 操作浏览器名称
     */
    @NotEmpty(message = "操作浏览器名称不能为空！")
    private String operateBrowser;

    /**
     * 服务器地址
     */
    @NotEmpty(message = "服务器地址不能为空！")
    private String serverAddress;

    /**
     * 执行时间(毫秒)
     */
    @NotNull(message = "执行时间(毫秒)不能为空！")
    private Long executeTime;

    /**
     * 操作状态（0：成功 1：失败）
     */
    @NotEmpty(message = "操作状态不能为空！")
    private String status;

    /**
     * 操作信息
     */
    private String message;
}
