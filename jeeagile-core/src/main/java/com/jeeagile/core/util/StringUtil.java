package com.jeeagile.core.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 字符集工具类
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 判断字符串对象是为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || "null".equalsIgnoreCase(str);
    }

    /**
     * 判断字符串对象不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 杀空函数，将"null"和null对象转换为""
     *
     * @param str 输入字符串
     * @return 输出字符串
     */
    public static String killNull(String str) {
        String returnStr;
        if (str == null || "null".equalsIgnoreCase(str)) {
            returnStr = "";
        } else {
            returnStr = str;
        }
        return returnStr;
    }

    /**
     * 杀空函数，将"null"和null对象转换为""
     *
     * @param object 输入字符串
     * @return 输出字符串
     */
    public static Object killNull(Object object) {
        Object rtnObj;
        if (object == null || "null".equalsIgnoreCase(object.toString())) {
            rtnObj = "";
        } else {
            rtnObj = object;
        }
        return rtnObj;
    }

    /**
     * 判断字符串长度是否大于0
     *
     * @param str
     * @return
     */
    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。
     * @param str 转换前的驼峰式命名的字符串，也可以为下划线形式
     * @return 转换后下划线方式命名的字符串
     */
    public static String toUnderlineCase(String str) {
        return toSymbolCase(str, CharUtil.UNDERLINE);
    }

    /**
     * 将驼峰式命名的字符串转换为使用符号连接方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串
     *
     * @param str    转换前的驼峰式命名的字符串，也可以为符号连接形式
     * @param symbol 连接符
     * @return 转换后符号连接方式命名的字符串
     */
    public static String toSymbolCase(String str, char symbol) {
        if (str == null) {
            return null;
        }
        str = lowerFirst(str);
        Matcher matcher = Pattern.compile("[A-Z]").matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, symbol + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
    /**
     * 小写首字母
     * @param str 字符串
     * @return 字符串
     */
    public static String lowerFirst(String str) {
        if (str != null && str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isUpperCase(firstChar)) {
                return Character.toLowerCase(firstChar) + str.substring(1);
            }
        }
        return str;
    }
    /**
     * 获取UUID 不带下划线
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
