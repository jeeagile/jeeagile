package com.jeeagile.frame.handler;

import com.alibaba.fastjson.JSONException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.result.IAgileResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@ControllerAdvice
@ResponseBody
public class AgileGlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public <T> AgileResult<T> runtimeExceptionHandler(RuntimeException ex) {
        logger.error("系统运行异常，异常信息：", ex);
        if (ex.getCause() instanceof AgileBaseException) {
            return agileResult((AgileBaseException) ex.getCause());
        }
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "系统运行异常！");
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public <T> AgileResult<T> nullPointerExceptionHandler(NullPointerException ex) {
        logger.error("空指针异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "系统发生空指针异常！");
    }

    /**
     * 类型转换异常
     */
    @ExceptionHandler(ClassCastException.class)
    public <T> AgileResult<T> classCastExceptionHandler(ClassCastException ex) {
        logger.error("类型转换异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "系统发生类型转换异常！");
    }

    /**
     * IO异常
     */
    @ExceptionHandler(IOException.class)
    public <T> AgileResult<T> iOExceptionHandler(IOException ex) {
        logger.error("IO异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "系统发生IO异常！");
    }

    /**
     * 数组越界异常
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public <T> AgileResult<T> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        logger.error("数组越界异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "系统发生数组越界异常！");
    }

    /**
     * 数据转换异常
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public <T> AgileResult<T> dataParseException(HttpMessageNotReadableException ex) {
        logger.error("数据转换异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "系统发生数据转换异常！");
    }

    /**
     * 方法不支持异常
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public <T> AgileResult<T> methodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        logger.error("方法不支持异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, ex.getMessage());
    }

    /**
     * 数据格式化异常
     */
    @ExceptionHandler({JSONException.class})
    public <T> AgileResult<T> jsonParseException(JSONException ex) {
        logger.error("JSON数据库格式化异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "JSON数据库格式化异常！");
    }

    /**
     * 未知方法异常
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public <T> AgileResult<T> noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        logger.error("未知方法异常，异常信息：", ex);
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "未知方法异常！");
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler({AgileBaseException.class})
    public <T> AgileResult<T> agileBaseException(AgileBaseException ex) {
        logger.warn("自定义异常，异常信息：{}", ex.getMessage().replace("\r\n", ""));
        return agileResult(ex);
    }

    /**
     * 其他未拦截错误
     */
    @ExceptionHandler({Exception.class})
    public <T> AgileResult<T> exception(Exception ex) {
        logger.error("系统级错误异常，异常信息：", ex);
        if (ex.getCause() instanceof AgileBaseException) {
            return agileResult((AgileBaseException) ex.getCause());
        }
        return agileResult(AgileResultCode.FAIL_SERVER_EXCEPTION, "系统级错误异常！");
    }

    private <T> AgileResult<T> agileResult(AgileBaseException ex) {
        return agileResult(ex.getAgileResultCode(), ex.getMessage());
    }

    private <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode, String message) {
        return AgileResult.error(agileResultCode, message);
    }
}
