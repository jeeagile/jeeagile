package com.jeeagile.logger.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
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
public class AgileLoggerOperate extends AgileBaseModel<AgileLoggerOperate> {
    /**
     * 日志标题
     */
    private String title;
    /**
     * 日志类型
     */
    private String type;
    /**
     * 操作人名称
     */
    private String userName;
    /**
     * 请求URI
     */
    private String reqUri;
    /**
     * 操作方式
     */
    private String reqMethod;
    /**
     * 请求数据
     */
    private String reqParam;
    /**
     * 返回数据
     */
    private String resParam;
    /**
     * 操作方法
     */
    private String handleMethod;
    /**
     * 操作IP地址
     */
    private String remoteIp;
    /**
     * 操作地理位置
     */
    private String remoteLocation;
    /**
     * 请求服务器IP地址
     */
    private String serverAddress;

    /**
     * 请求设备名称
     */
    private String deviceName;

    /**
     * 请求设备操作系统
     */
    private String osName;

    /**
     * 请求浏览器名称
     */
    private String browserName;
    /**
     * 执行时间(毫秒)
     */
    private Long executeTime;
    /**
     * 日志状态
     */
    private String status;
    /**
     * 异常信息
     */
    private String message;

}
