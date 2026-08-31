package com.jeeagile.drools.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建人 wangcy
 * @创建日期 2024-01-24
 * @描述
 */
public class DroolsModelType {
    /**
     * java。
     */
    public static final String JAVA = "java";
    /**
     * declare。
     */
    public static final String DECLARE = "declare";


    /**
     *
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(2);

    static {
        DICT_MAP.put(JAVA, "JAVA");
        DICT_MAP.put(DECLARE, "DECLARE");
    }

    /**
     * 判断字典值是否合法。
     */
    public static boolean isValid(String value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 获取字典描述
     */
    public static String getDesc(String value) {
        return DICT_MAP.get(value);
    }

    /**
     * 私有构造函数
     */
    private DroolsModelType() {
    }
}
