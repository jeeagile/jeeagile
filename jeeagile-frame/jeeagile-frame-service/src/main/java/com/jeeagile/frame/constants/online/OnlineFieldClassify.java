package com.jeeagile.frame.constants.online;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2027-07-27
 * @description 在线表单 数据表字段分类
 */
public class OnlineFieldClassify {
    /**
     * 文件上传字段
     */
    public static final String UPLOAD_FILE = "01";
    /**
     * 图片上传字段
     */
    public static final String UPLOAD_IMAGE = "02";
    /**
     * 富文本字段
     */
    public static final String RICH_TEXT = "03";
    /**
     * 创建人字段
     */
    public static final String CREATE_USER = "04";
    /**
     * 创建时间字段
     */
    public static final String CREATE_TIME = "05";
    /**
     * 更新人字段
     */
    public static final String UPDATE_USER = "06";
    /**
     * 更新时间字段
     */
    public static final String UPDATE_TIME = "07";
    /**
     * 逻辑删除字段
     */
    public static final String LOGIC_DELETE = "08";

    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(UPLOAD_FILE, "文件上传字段");
        DICT_MAP.put(UPLOAD_IMAGE, "图片上传字段");
        DICT_MAP.put(RICH_TEXT, "富文本字段");
        DICT_MAP.put(CREATE_USER, "创建人字段");
        DICT_MAP.put(CREATE_TIME, "创建时间字段");
        DICT_MAP.put(UPDATE_USER, "更新人字段");
        DICT_MAP.put(UPDATE_TIME, "更新时间字段");
        DICT_MAP.put(LOGIC_DELETE, "逻辑删除字段");
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
     * @param columnName
     * @return
     */
    public static String convertFieldClassify(String columnName) {
        switch (columnName.toLowerCase()) {
            case "create_user":
                return CREATE_USER;
            case "create_time":
                return CREATE_TIME;
            case "update_user":
                return UPDATE_USER;
            case "update_time":
                return UPDATE_TIME;
            default:
                return null;
        }
    }

    /**
     * 私有构造函数
     */
    private OnlineFieldClassify() {
    }
}
