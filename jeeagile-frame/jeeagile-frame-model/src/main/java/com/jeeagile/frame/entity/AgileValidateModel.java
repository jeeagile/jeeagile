package com.jeeagile.frame.entity;

import com.jeeagile.core.util.validate.AgileValidateUtil;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface AgileValidateModel {
    /**
     * 尝试验证此Model
     *
     * @param group 验证分组
     * @param <T>   当前对象类型
     * @return 当前对象
     */
    default <T extends AgileValidateModel> T validate(Class... group) {
        AgileValidateUtil.validateObject(this, group);
        return (T) this;
    }
}
