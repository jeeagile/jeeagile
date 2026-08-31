package com.jeeagile.drools.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建人 wangcy
 * @创建日期 2024-02-22
 * @描述
 */
public class DroolsFieldType {
    /**
     * String。
     */
    public static final String String = "String";
    /**
     * BigDecimal。
     */
    public static final String BigDecimal = "BigDecimal";
    /**
     * BigInteger。
     */
    public static final String BigInteger = "BigInteger";
    /**
     * Boolean。
     */
    public static final String Boolean = "Boolean";
    /**
     * Date。
     */
    public static final String Date = "Date";
    /**
     * Double。
     */
    public static final String Double = "Double";
    /**
     * Float。
     */
    public static final String Float = "Float";
    /**
     * Integer。
     */
    public static final String Integer = "Integer";

    /**
     * LocalDate。
     */
    public static final String LocalDate = "LocalDate";
    /**
     * LocalDateTime。
     */
    public static final String LocalDateTime = "LocalDateTime";
    /**
     * LocalTime。
     */
    public static final String LocalTime = "LocalTime";
    /**
     * Long。
     */
    public static final String Long = "Long";
    /**
     * Short。
     */
    public static final String SHORT = "Short";
    /**
     * boolean。
     */
    public static final String BOOLEAN = "boolean";
    /**
     * byte。
     */
    public static final String BYTE = "byte";
    /**
     * char。
     */
    public static final String CHAR = "char";

    /**
     * double。
     */
    public static final String DOUBLE = "double";
    /**
     * float。
     */
    public static final String FLOAT = "float";
    /**
     * int。
     */
    public static final String INT = "int";
    /**
     * long。
     */
    public static final String LONG = "long";

    /**
     * long。
     */
    public static final String Map = "Map";
    /**
     * long。
     */
    public static final String Object = "Object";
    /**
     *
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(2);

    static {
        DICT_MAP.put(String, "String");
        DICT_MAP.put(BigDecimal, "BigDecimal");
        DICT_MAP.put(BigInteger, "BigInteger");
        DICT_MAP.put(Boolean, "Boolean");
        DICT_MAP.put(Date, "Date");
        DICT_MAP.put(Double, "Double");
        DICT_MAP.put(Float, "Float");
        DICT_MAP.put(Integer, "Integer");
        DICT_MAP.put(LocalDate, "LocalDate");
        DICT_MAP.put(LocalDateTime, "LocalDateTime");
        DICT_MAP.put(LocalTime, "LocalTime");
        DICT_MAP.put(Long, "Long");
        DICT_MAP.put(SHORT, "short");
        DICT_MAP.put(BOOLEAN, "boolean");
        DICT_MAP.put(BYTE, "byte");
        DICT_MAP.put(CHAR, "char");
        DICT_MAP.put(DOUBLE, "DOUBLE");
        DICT_MAP.put(FLOAT, "FLOAT");
        DICT_MAP.put(INT, "INT");
        DICT_MAP.put(LONG, "LONG");
        DICT_MAP.put(Map, "Map");
        DICT_MAP.put(Object, "数据对象");
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
    private DroolsFieldType() {
    }
}
