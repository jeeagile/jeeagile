package com.jeeagile.frame.constants;

import com.jeeagile.core.util.AgileStringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-22
 * @description 正常停用
 */
public class SysNormalDisable {
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
    private static final Map<String, String> SYS_YES_NO_MAP = new HashMap<>(2);

    static {
        SYS_YES_NO_MAP.put(NORMAL, "正常");
        SYS_YES_NO_MAP.put(DISABLE, "停用");
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
            desc = "否";
        }
        return desc;
    }

    /**
     * 私有构造函数
     */
    private SysNormalDisable() {
    }
}
