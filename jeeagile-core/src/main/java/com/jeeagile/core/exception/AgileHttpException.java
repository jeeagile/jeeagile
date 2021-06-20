package com.jeeagile.core.exception;


import com.jeeagile.core.result.AgileResultCode;
/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 异常处理类
 */
public class AgileHttpException extends AgileBaseException {

    public AgileHttpException(Throwable throwable) {
        super(AgileResultCode.FAIL_HTTP_EXCEPTION, throwable);
    }

    public AgileHttpException(String message) {
        super(AgileResultCode.FAIL_HTTP_EXCEPTION, message);
    }

    public AgileHttpException(String message, Throwable throwable) {
        super(AgileResultCode.FAIL_HTTP_EXCEPTION, message, throwable);
    }
}
