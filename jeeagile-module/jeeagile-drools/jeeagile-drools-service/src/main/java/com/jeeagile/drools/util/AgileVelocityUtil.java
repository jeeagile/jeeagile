package com.jeeagile.drools.util;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.exception.AgileFrameException;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;

import java.util.Properties;

/**
 * @创建人 wangcy
 * @创建日期 2024-02-22
 * @描述
 */
public class AgileVelocityUtil {
    private static final String VELOCITY_INIT_CLASS_KEY = "file.resource.loader.class";
    private static final String VELOCITY_INIT_CLASS_NAME = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    /**
     * 初始化Velocity
     */
    public static void initVelocity() {
        try {
            Properties properties = new Properties();
            // 加载classpath目录下的vm文件
            properties.setProperty(VELOCITY_INIT_CLASS_KEY, VELOCITY_INIT_CLASS_NAME);
            // 定义字符集
            properties.setProperty(RuntimeConstants.ENCODING_DEFAULT, AgileConstants.UTF8);
            properties.setProperty(RuntimeConstants.OUTPUT_ENCODING, AgileConstants.UTF8);
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(properties);
        } catch (Exception e) {
            throw new AgileFrameException(e);
        }
    }
}
