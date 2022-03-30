package com.jeeagile.core.kaptcha;

import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.core.util.spring.AgileSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
public class AgileKaptchaUtil {
    private AgileKaptchaUtil() {
    }

    /**
     * 获取验证码生成器
     */
    public static AgileKaptchaProducer getAgileKaptchaProducer(AgileKaptchaType agileKaptchaType) {
        String kaptchaBeanName = agileKaptchaType.name() + "CaptchaProducer";
        AgileKaptchaProducer agileKaptchaProducer = AgileSpringUtil.getBean(kaptchaBeanName, AgileKaptchaProducer.class);
        if (agileKaptchaProducer == null) {
            log.error("验证码类型配置有误，请检查！");
        }
        agileKaptchaProducer.setAgileKaptchaType(agileKaptchaType);
        return agileKaptchaProducer;
    }

    /**
     * 获取验证码生成器
     */
    public static AgileKaptchaProducer getAgileKaptchaProducer() {
        return getAgileKaptchaProducer(AgileUtil.getCaptchaType());
    }

    /**
     * 生成验证码
     */
    public static AgileKaptchaInfo createImage(AgileKaptchaProducer agileKaptchaProducer) {
        AgileKaptchaInfo agileKaptchaInfo = null;
        switch (agileKaptchaProducer.getAgileKaptchaType()) {
            case arithmetic:
                agileKaptchaInfo = new AgileKaptchaInfo();
                agileKaptchaInfo.setUuid(AgileStringUtil.getUuid());
                String capText = agileKaptchaProducer.createText();
                String capStr = capText.substring(0, capText.lastIndexOf("@"));
                agileKaptchaInfo.setCode(capText.substring(capText.lastIndexOf("@") + 1));
                BufferedImage bufferedImage = agileKaptchaProducer.createImage(capStr);
                agileKaptchaInfo.setImage(createBase64Image(bufferedImage));
                break;
            case chinese:
//                captcha = new ChineseCaptcha(loginCode.getWidth(), loginCode.getHeight());
//                captcha.setLen(loginCode.getLength());
                break;
            default:
                throw new AgileFrameException("验证码配置信息错误！");
        }
        return agileKaptchaInfo;
    }

    /**
     * 生成Base64Image
     *
     * @param bufferedImage
     * @return
     */
    private static String createBase64Image(BufferedImage bufferedImage) {
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", os);
        } catch (IOException e) {
            throw new AgileFrameException("验证码转换base64出错！");
        }
        return Base64Utils.encodeToString(os.toByteArray());
    }

    /**
     * 生成验证码
     */
    public static AgileKaptchaInfo createImage() {
        return createImage(getAgileKaptchaProducer());
    }

    /**
     * 生成验证码
     */
    public static AgileKaptchaInfo createImage(AgileKaptchaType agileKaptchaType) {
        return createImage(getAgileKaptchaProducer(agileKaptchaType));
    }
}
