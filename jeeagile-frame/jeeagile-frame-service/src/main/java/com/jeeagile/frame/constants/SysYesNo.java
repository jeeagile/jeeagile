package com.jeeagile.frame.constants;

import com.jeeagile.core.util.AgileStringUtil;

import java.util.HashMap;
import java.util.Map;

public class SysYesNo {
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
    private static final Map<String, String> SYS_YES_NO_MAP = new HashMap<>(2);

    static {
        SYS_YES_NO_MAP.put(NO, "否");
        SYS_YES_NO_MAP.put(YES, "是");
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
    private SysYesNo() {
    }
}
