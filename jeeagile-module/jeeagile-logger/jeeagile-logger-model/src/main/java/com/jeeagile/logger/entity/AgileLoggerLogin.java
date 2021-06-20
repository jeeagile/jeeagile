package com.jeeagile.logger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jeeagile.frame.entity.AgileModel;
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
     * 登录用户名
     */
    private String loginName;

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
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录状态
     */
    private String status;

    /**
     * 登录信息
     */
    private String message;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
