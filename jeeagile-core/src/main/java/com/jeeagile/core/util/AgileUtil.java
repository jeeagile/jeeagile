package com.jeeagile.core.util;

import com.jeeagile.core.kaptcha.AgileKaptchaType;
import com.jeeagile.core.properties.AgileProperties;
import org.springframework.stereotype.Component;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Component
public class AgileUtil {
    private static AgileProperties agileProperties;

    private AgileUtil(AgileProperties agileProperties) {
        this.agileProperties = agileProperties;
    }

    /**
     * 获取系统配置文件
     *
     * @return
     */
    public static AgileProperties getAgileProperties() {
        if (AgileUtil.agileProperties == null) {
            AgileUtil.agileProperties = new AgileProperties();
        }
        return AgileUtil.agileProperties;
    }

    /**
     * 获取项目名称
     *
     * @return
     */
    public static String getProjectName() {
        return getAgileProperties().getName();
    }

    /**
     * 获取项目版本
     *
     * @return
     */
    public static String getProjectVersion() {
        return getAgileProperties().getVersion();
    }

    /**
     * 是否开启演示模式
     *
     * @return
     */
    public static boolean isDemoEnabled() {
        return getAgileProperties().isDemoEnabled();
    }

    /**
     * 是否开启获取地址
     *
     * @return
     */
    public static boolean isAddressEnabled() {
        return getAgileProperties().isAddressEnabled();
    }

    /**
     * 获取上传文件路径
     *
     * @return
     */
    public static String getUploadPath() {
        return getAgileProperties().getUploadPath();
    }

    /**
     * 是否开启验证码
     *
     * @return
     */
    public static boolean isCaptchaEnabled() {
        return getAgileProperties().isCaptchaEnabled();
    }

    /**
     * 获取验证码类型
     *
     * @return
     */
    public static AgileKaptchaType getCaptchaType() {
        AgileKaptchaType agileKaptchaType = getAgileProperties().getCaptchaType();
        if (agileKaptchaType == null) {
            getAgileProperties().setCaptchaType(AgileKaptchaType.arithmetic);
        }
        return getAgileProperties().getCaptchaType();
    }


}
