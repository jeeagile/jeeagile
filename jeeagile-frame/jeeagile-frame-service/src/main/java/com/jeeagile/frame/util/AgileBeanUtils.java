package com.jeeagile.frame.util;

import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.frame.entity.AgileBaseModel;
import org.springframework.beans.BeanUtils;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 自定义Bean工具类
 */
public class AgileBeanUtils {
    public static void copyProperties(Object source, Object target) throws AgileFrameException {
        try {
            BeanUtils.copyProperties(source, target);
            if (target instanceof AgileBaseModel) {
                AgileBaseModel agileBaseModel = (AgileBaseModel) target;
                if (agileBaseModel != null) {
                    agileBaseModel.setId(null);
                    agileBaseModel.setCreateUser(null);
                    agileBaseModel.setCreateTime(null);
                    agileBaseModel.setUpdateUser(null);
                    agileBaseModel.setUpdateTime(null);
                }
            }
        } catch (Exception ex) {
            throw new AgileFrameException(ex);
        }
    }
}
