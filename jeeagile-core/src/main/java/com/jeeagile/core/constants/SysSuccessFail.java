package com.jeeagile.core.constants;

import com.jeeagile.core.util.AgileStringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-22
 * @description 成功失败
 */
public class SysSuccessFail {
    /**
     * 成功
     */
    public static final String SUCCESS = "0";
    /**
     * 失败
     */
    public static final String FAIL = "1";
    /**
     * MAP
     */
    private static final Map<String, String> SYS_YES_NO_MAP = new HashMap<>(2);

    static {
        SYS_YES_NO_MAP.put(SUCCESS, "成功");
        SYS_YES_NO_MAP.put(FAIL, "失败");
    }

    /**
     * 判断是否当前常量字典的合法值。
     */
    public static boolean isValid(String value) {
        return value != null && SYS_YES_NO_MAP.containsKey(value);
    }

    public static String getDesc(String value) {
        String desc = SYS_YES_NO_MAP.get(value);
        if (AgileStringUtil.isEmpty(desc)) {
            desc = "失败";
        }
        return desc;
    }

    /**
     * 私有构造函数
     */
    private SysSuccessFail() {
    }
}
