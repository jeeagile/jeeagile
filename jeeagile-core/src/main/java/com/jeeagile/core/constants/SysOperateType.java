package com.jeeagile.core.constants;


import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 日志类型
 */
public class SysOperateType {
    /**
     * 查询数据
     */
    public static final String SELECT = "SELECT";

    /**
     * 查看明细
     */
    public static final String DETAIL = "DETAIL";

    /**
     * 新增数据
     */
    public static final String ADD = "ADD";

    /**
     * 修改数据
     */
    public static final String UPDATE = "UPDATE";

    /**
     * 删除数据
     */
    public static final String DELETE = "DELETE";

    /**
     * 清空数据
     */
    public static final String CLEAR = "CLEAR";
    /**
     * 用户授权
     */
    public static final String GRANT = "GRANT";
    /**
     * 导出数据
     */
    public static final String EXPORT = "EXPORT";
    /**
     * 导入数据
     */
    public static final String IMPORT = "IMPORT";
    /**
     * 用户登录
     */
    public static final String LOGIN = "LOGIN";
    /**
     * 用户强退
     */
    public static final String FORCE = "FORCE";
    /**
     * 生成代码
     */
    public static final String GENERATOR = "GENERATOR";
    /**
     * 清空数据
     */
    public static final String CLEAN = "CLEAN";
    /**
     * 其它操作
     */
    public static final String OTHER = "OTHER";

    /**
     *
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(2);

    static {
        DICT_MAP.put(SELECT, "查询数据");
        DICT_MAP.put(DETAIL, "查看明细");
        DICT_MAP.put(ADD, "新增数据");
        DICT_MAP.put(UPDATE, "修改数据");
        DICT_MAP.put(DELETE, "删除数据");
        DICT_MAP.put(CLEAR, "清空数据");
        DICT_MAP.put(GRANT, "用户授权");
        DICT_MAP.put(EXPORT, "导出数据");
        DICT_MAP.put(IMPORT, "导入数据");
        DICT_MAP.put(LOGIN, "用户登录");
        DICT_MAP.put(FORCE, "用户强退");
        DICT_MAP.put(GENERATOR, "生成代码");
        DICT_MAP.put(CLEAN, "清空数据");
        DICT_MAP.put(OTHER, "其它操作");
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
    private SysOperateType() {
    }
}
