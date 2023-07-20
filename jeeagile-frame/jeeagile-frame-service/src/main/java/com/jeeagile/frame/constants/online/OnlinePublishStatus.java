package com.jeeagile.frame.constants.online;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2027-07-17
 * @description 在线表单 发布状态字典常量
 */
public class OnlinePublishStatus {
    /**
     * 已发布
     */
    public static final String PUBLISHED = "1";
    /**
     * 未发布
     */
    public static final String UNPUBLISHED = "2";

    /**
     *
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(2);

    static {
        DICT_MAP.put(PUBLISHED, "已发布");
        DICT_MAP.put(UNPUBLISHED, "未发布");
    }

    /**
     * 判断值是否合法。
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
    private OnlinePublishStatus() {
    }
}
