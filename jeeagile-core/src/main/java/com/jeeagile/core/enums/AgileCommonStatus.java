package com.jeeagile.core.enums;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 成功失败枚举类
 */
public enum AgileCommonStatus {
    SUCCESS("0", "成功"),
    FAIL("1", "失败");
    /**
     * 码值
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    AgileCommonStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AgileCommonStatus agileFlagEnum : AgileCommonStatus.values()) {
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
