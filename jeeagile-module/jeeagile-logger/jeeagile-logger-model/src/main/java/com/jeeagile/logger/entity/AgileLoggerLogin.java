package com.jeeagile.logger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jeeagile.frame.entity.AgileModel;
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
public class AgileLoggerLogin extends AgileModel<AgileLoggerLogin> {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 登录类型
     */
    @ApiModelProperty(value = "登录类型", position = 1)
    private String loginModule;

    /**
     * 登录用户名
     */
    @ApiModelProperty(value = "登录名称", position = 2)
    private String loginName;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间", position = 3)
    private Date loginTime;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP", position = 4)
    private String loginIp;

    /**
     * 登录地址
     */
    @ApiModelProperty(value = "登录地址", position = 5)
    private String loginAddress;

    /**
     * 登录设备名称
     */
    @ApiModelProperty(value = "登录设备名称", position = 6)
    private String loginDevice;

    /**
     * 登录设备操作系统名称
     */
    @ApiModelProperty(value = "登录设备操作系统名称", position = 7)
    private String loginOs;

    /**
     * 登录浏览器名称
     */
    @ApiModelProperty(value = "登录浏览器名称", position = 8)
    private String loginBrowser;

    /**
     * 登录服务器IP地址
     */
    @ApiModelProperty(value = "登录服务器IP", position = 9)
    private String serverAddress;
    /**
     * 登录状态
     */
    @ApiModelProperty(value = "登录状态", position = 10)
    private String status;

    /**
     * 登录信息
     */
    @ApiModelProperty(value = "登录信息", position = 11)
    private String message;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
