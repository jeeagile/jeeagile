package com.jeeagile.core.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 字符集工具类
 */
public class CharsetUtil {
    private CharsetUtil() {
    }

    /**
     * UTF-8
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * GBK
     */
    public static final String GBK = "GBK";

    /**
     * UTF-8
     */
    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    /**
     * GBK
     */
    public static final Charset CHARSET_GBK;

    static {
        //避免不支持GBK的系统中运行报错
        Charset gbkCharset = null;
        try {
            gbkCharset = Charset.forName(GBK);
        } catch (UnsupportedCharsetException e) {
            //ignore
        }
        CHARSET_GBK = gbkCharset;
    }

    /**
     * 转换为Charset对象
     *
     * @param charsetName 字符集，为空则返回默认字符集
     * @return Charset
     */
    public static Charset charset(String charsetName) {
        return StringUtil.isEmpty(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

}
