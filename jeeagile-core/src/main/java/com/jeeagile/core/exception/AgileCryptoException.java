package com.jeeagile.core.exception;

import com.jeeagile.core.result.AgileResultCode;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 加解密异常类
 */
public class AgileCryptoException extends AgileBaseException {

    /**
     * 构造函数
     * @param message
     */
    public AgileCryptoException(String message) {
        super(AgileResultCode.FAIL_CRYPTO_EXCEPTION, message);
    }

    /**
     * 构造函数
     * @param throwable
     */
    public AgileCryptoException(Throwable throwable) {
        super(AgileResultCode.FAIL_CRYPTO_EXCEPTION, throwable);
    }

    /**
     * 构造函数
     * @param message
     * @param throwable
     */
    public AgileCryptoException(String message, Throwable throwable) {
        super(AgileResultCode.FAIL_CRYPTO_EXCEPTION, message, throwable);
    }
}
