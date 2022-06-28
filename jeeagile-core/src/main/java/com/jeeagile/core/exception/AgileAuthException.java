package com.jeeagile.core.exception;

import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.result.IAgileResultCode;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileAuthException extends AgileBaseException {

    /**
     * 构造函数 自定义异常类
     *
     * @param message
     */
    public AgileAuthException(String message) {
        super(AgileResultCode.FAIL_AUTH_EXCEPTION, message);
    }

    /**
     * 构造函数
     *
     * @param message
     */
    public AgileAuthException(String message,Throwable throwable) {
        super(AgileResultCode.FAIL_AUTH_EXCEPTION, message);
    }
    /**
     * 构造函数
     *
     * @param agileResultCode
     */
    public AgileAuthException(IAgileResultCode agileResultCode) {
        super(agileResultCode);
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param throwable
     */
    public AgileAuthException(IAgileResultCode agileResultCode, Throwable throwable) {
        super(agileResultCode, throwable);
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param message
     */
    public AgileAuthException(IAgileResultCode agileResultCode, String message) {
        super(agileResultCode, message);
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param message
     * @param throwable
     */
    public AgileAuthException(IAgileResultCode agileResultCode, String message, Throwable throwable) {
        super(agileResultCode, message, throwable);
    }
}
