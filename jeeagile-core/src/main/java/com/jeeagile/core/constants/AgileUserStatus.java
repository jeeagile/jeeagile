package com.jeeagile.core.constants;

import com.jeeagile.core.util.AgileStringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-22
 * @description 用户状态
 */
public class AgileUserStatus {
    /**
     * 正常
     */
    public static final String NORMAL = "0";
    /**
     * 停用
     */
    public static final String DISABLE = "1";
    /**
     * MAP
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(2);

    static {
        DICT_MAP.put(NORMAL, "正常");
        DICT_MAP.put(DISABLE, "停用");
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
            desc = "停用";
        }
        return desc;
    }

    /**
     * 私有构造函数
     */
    private AgileUserStatus() {
    }
}
