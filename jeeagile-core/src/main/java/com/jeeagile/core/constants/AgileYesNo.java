package com.jeeagile.core.constants;

import com.jeeagile.core.util.AgileStringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-07-17
 * @description 是否字典常量
 */
public class AgileYesNo {
    /**
     * 否
     */
    public static final String NO = "0";
    /**
     * 是
     */
    public static final String YES = "1";
    /**
     * MAP
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(2);

    static {
        DICT_MAP.put(NO, "否");
        DICT_MAP.put(YES, "是");
    }

    /**
     * 判断是否当前常量字典的合法值。
     */
    public static boolean isValid(String value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    public static String getDesc(String value) {
        String desc = DICT_MAP.get(value);
        if (AgileStringUtil.isEmpty(desc)) {
            desc = "否";
        }
        return desc;
    }

    /**
     * 私有构造函数
     */
    private AgileYesNo() {
    }
}
