package com.jeeagile.core.kaptcha;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
public class AgileKaptchaInfo {
    /**
     * 唯一ID
     */
    private String uuid;
    /**
     * 验证码
     */
    private String code;
    /**
     * 图片
     */
    private String image;
}
