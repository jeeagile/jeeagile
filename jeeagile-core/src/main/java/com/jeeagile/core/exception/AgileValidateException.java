package com.jeeagile.core.exception;

import com.jeeagile.core.result.AgileResultCode;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 验证异常类
 */
public class AgileValidateException extends AgileBaseException {
    /**
     * 构造函数
     * @param message
     */
    public AgileValidateException(String message) {
        super(AgileResultCode.WARN_VALIDATE_PASSED, message);
    }
}
