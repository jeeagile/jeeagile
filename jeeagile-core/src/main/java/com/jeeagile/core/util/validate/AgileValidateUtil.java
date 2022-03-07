package com.jeeagile.core.util.validate;

import com.jeeagile.core.exception.AgileValidateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 数据验证工具类
 */
public class AgileValidateUtil {
    private AgileValidateUtil() {
    }

    //初始化验证参数的框架
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     */
    public static void validateObject(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations;
        if (groups != null) {
            constraintViolations = validator.validate(object, groups);
        } else {
            constraintViolations = validator.validate(object);
        }
        if (!constraintViolations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                stringBuilder.append(constraint.getMessage());
                stringBuilder.append("\r\n");
            }
            throw new AgileValidateException(stringBuilder.toString());
        }
    }
}
