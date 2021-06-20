package com.jeeagile.core.enums;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 是否枚举类
 */
public enum AgileFlagEnum {
    NO("0", "否"),
    YES("1", "是");
    /**
     * 码值
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    AgileFlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AgileFlagEnum agileFlagEnum : AgileFlagEnum.values()) {
            if (agileFlagEnum.code.equals(code)) {
                return agileFlagEnum.desc;
            }
        }
        return "";
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

}
