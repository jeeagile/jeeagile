package com.jeeagile.core.properties;

import com.jeeagile.core.kaptcha.AgileKaptchaType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "agile")
public class AgileProperties implements Serializable {
    /**
     * 项目名称
     */
    private String name;
    /**
     * 版本
     */
    private String version;
    /**
     * 实例演示开关
     */
    private boolean demoEnabled;
    /**
     * 获取地址开关
     */
    private boolean addressEnabled;
    /**
     * 上传路径
     */
    private String uploadPath;
    /**
     * 验证码开关
     */
    private boolean captchaEnabled = true;
    /**
     * 验证码类型
     */
    private AgileKaptchaType captchaType;
    /**
     * 高级管理员账号名称
     */
    private String superAdmin = "admin";

}
