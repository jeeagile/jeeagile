package com.jeeagile.core.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 字符集工具类
 */
public class AgileStringUtil {

    private AgileStringUtil() {
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
     * 判断字符串对象是为空
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        return object == null || "".equals(object.toString()) || "null".equalsIgnoreCase(object.toString());
    }

    /**
     * 判断字符串对象不为空
     *
     * @param object
     * @return
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
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
        return toSymbolCase(str, AgileCharUtil.UNDERLINE);
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
     * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
     * 例如：hello_world=》helloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toCamelCase(String name) {
        if (null == name) {
            return null;
        }
        final String nameTemp = name;
        if (nameTemp.contains(AgileCharUtil.UNDERLINE + "")) {
            final int length = nameTemp.length();
            final StringBuilder sb = new StringBuilder(length);
            boolean upperCase = false;
            for (int i = 0; i < length; i++) {
                char c = nameTemp.charAt(i);
                if (c == AgileCharUtil.UNDERLINE) {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        } else {
            return nameTemp;
        }
    }

    /**
     * 大写首字母<br>
     * 例如：str = name, return Name
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String upperFirst(String str) {
        if (str != null && str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                return Character.toUpperCase(firstChar) + str.substring(1);
            }
        }
        return str;
    }

    /**
     * 格式化字符串 将占位符 {} 按照顺序替换为参数
     */
    public static String format(final String strPattern, final String... argArray) {
        if (AgileStringUtil.isEmpty(strPattern) || argArray == null || argArray.length < 1) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();
        // 初始化定义好的长度以获得更好的性能
        StringBuilder stringBuilder = new StringBuilder(strPatternLength + 50);
        int handledPosition = 0;// 记录已经处理到的位置
        int placeholderIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            placeholderIndex = strPattern.indexOf("{}", handledPosition);
            if (placeholderIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return strPattern;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                stringBuilder.append(strPattern, handledPosition, strPatternLength);
                return stringBuilder.toString();
            }
            // 转义符
            if (placeholderIndex > 0 && strPattern.charAt(placeholderIndex - 1) == AgileCharUtil.BACKSLASH) {// 转义符
                if (placeholderIndex > 1 && strPattern.charAt(placeholderIndex - 2) == AgileCharUtil.BACKSLASH) {// 双转义符
                    // 转义符之前还有一个转义符，占位符依旧有效
                    stringBuilder.append(strPattern, handledPosition, placeholderIndex - 1);
                    stringBuilder.append(argArray[argIndex]);
                    handledPosition = placeholderIndex + 2;
                } else {
                    // 占位符被转义
                    argIndex--;
                    stringBuilder.append(strPattern, handledPosition, placeholderIndex - 1);
                    stringBuilder.append(AgileCharUtil.DELIM_START);
                    handledPosition = placeholderIndex + 1;
                }
            } else {// 正常占位符
                stringBuilder.append(strPattern, handledPosition, placeholderIndex);
                stringBuilder.append(argArray[argIndex]);
                handledPosition = placeholderIndex + 2;
            }
        }
        // 加入最后一个占位符后所有的字符
        stringBuilder.append(strPattern, handledPosition, strPattern.length());
        return stringBuilder.toString();
    }

    /**
     * 获取UUID 不带下划线
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
