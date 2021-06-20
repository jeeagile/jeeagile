package com.jeeagile.core.kaptcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileKaptchaProducer extends DefaultKaptcha {
    @Getter
    @Setter
    private AgileKaptchaType agileKaptchaType;
}
