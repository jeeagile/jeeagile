package com.jeeagile.core.result;

import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.util.AgileStringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@Slf4j
public class AgileResult<T> implements Serializable {
    /**
     * 响应编码
     */
    private String code;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应时间
     */
    private long timestamp = System.currentTimeMillis();
    /**
     * 响应业务数据
     */
    @SuppressWarnings("all")
    private T data;

    /**
     * 构造函数
     *
     * @param agileResultCode
     */
    public AgileResult(IAgileResultCode agileResultCode) {
        this.code = agileResultCode.getCode();
        this.message = getMessage(agileResultCode);
    }


    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param message
     */
    public AgileResult(IAgileResultCode agileResultCode, String message) {
        this.code = agileResultCode.getCode();
        this.message = agileResultCode.getMessage();
        if (AgileStringUtil.hasLength(message)) {
            this.message = message;
        } else {
            this.message = getMessage(agileResultCode);
        }
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param data
     */
    public AgileResult(IAgileResultCode agileResultCode, T data) {
        this.code = agileResultCode.getCode();
        this.message = getMessage(agileResultCode);
        this.data = data;
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param message
     * @param data
     */
    public AgileResult(IAgileResultCode agileResultCode, String message, T data) {
        this.code = agileResultCode.getCode();
        this.message = agileResultCode.getMessage();
        if (AgileStringUtil.hasLength(message)) {
            this.message = message;
        } else {
            this.message = getMessage(agileResultCode);
        }
        this.data = data;
    }

    private String getMessage(IAgileResultCode agileResultCode) {
        if (AgileStringUtil.hasLength(agileResultCode.getMessage())) {
            return agileResultCode.getMessage();
        } else {
            return "未知错误操作信息！";
        }
    }

    public void put(String key, Object value) {
        if (this.data == null) {
            Map<String, Object> data = new HashMap<>();
            data.put(key, value);
            this.data = (T) data;
        } else {
            if (data instanceof Map) {
                ((Map) data).put(key, value);
            } else {
                log.warn("暂不支持非Map类型PUT值！");
            }
        }
    }

    /**
     * 返回成功结果集
     *
     * @return
     */
    public static <T> AgileResult<T> success() {
        return new AgileResult<>(AgileResultCode.SUCCESS);
    }

    /**
     * 返回成功结果集
     *
     * @param message 指定返回消息
     * @return
     */
    public static <T> AgileResult<T> success(String message) {
        return new AgileResult<>(AgileResultCode.SUCCESS, message);
    }

    /**
     * 返回成功结果集
     *
     * @param data 返回的数据结果集
     * @return
     */
    public static <T> AgileResult<T> success(Object data) {
        return new AgileResult<>(AgileResultCode.SUCCESS, (T) data);
    }

    /**
     * 返回成功结果集
     *
     * @param message 指定返回消息
     * @param data    返回的数据结果集
     * @return
     */
    public static <T> AgileResult<T> success(Object data, String message) {
        return new AgileResult<>(AgileResultCode.SUCCESS, message, (T) data);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @return
     */
    public static <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode) {
        return new AgileResult<>(agileResultCode);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @param data            返回的数据结果集
     * @return
     */
    public static <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode, Object data) {
        return new AgileResult<>(agileResultCode, (T) data);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @param data            返回的数据结果集
     * @param message         返回描述
     * @return
     */
    public static <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode, Object data, String message) {
        return new AgileResult<>(agileResultCode, message, (T) data);
    }

    /**
     * 指定类型返回结果
     *
     * @param agileResultCode
     * @return
     */
    public static <T> AgileResult<T> error(IAgileResultCode agileResultCode) {
        return new AgileResult<>(agileResultCode);
    }

    /**
     * 指定类型返回结果
     *
     * @param agileResultCode
     * @param message
     * @return
     */
    public static <T> AgileResult<T> error(IAgileResultCode agileResultCode, String message) {
        return new AgileResult<>(agileResultCode, message);
    }

    /**
     * 异常处理
     *
     * @param agileResultCode
     * @param ex
     * @return
     */
    public static <T> AgileResult<T> error(IAgileResultCode agileResultCode, Exception ex) {
        return new AgileResult<>(agileResultCode, AgileResult.handlerExceptionMessage(ex));
    }

    /**
     * 异常处理 如果异常信息获取不到异常信息则使用默认msg
     *
     * @param agileResultCode
     * @param ex              异常信息
     * @param defaultMsg      默认信息
     * @return
     */
    public static <T> AgileResult<T> error(IAgileResultCode agileResultCode, Exception ex, String defaultMsg) {
        String message = AgileResult.handlerExceptionMessage(ex);
        if (AgileStringUtil.isEmpty(message)) {
            message = defaultMsg;
        }
        return new AgileResult<>(agileResultCode, message);
    }


    /**
     * 异常处理
     *
     * @param ex
     * @param defaultMsg
     * @return
     */
    public static <T> AgileResult<T> error(Exception ex, String defaultMsg) {
        IAgileResultCode agileResultCode = AgileResultCode.FAIL_SAVE_EXCEPTION;
        if (ex instanceof AgileBaseException) {
            agileResultCode = ((AgileBaseException) ex).getAgileResultCode();
        }
        String message = AgileResult.handlerExceptionMessage(ex);
        if (AgileStringUtil.isEmpty(message)) {
            message = defaultMsg;
        }
        return new AgileResult<>(agileResultCode, message);
    }

    /**
     * 处理异常信息
     *
     * @param ex
     * @return
     */
    private static String handlerExceptionMessage(Exception ex) {
        String msg = "";
        if (handlerTimeOutException(ex) > -1) {
            msg = "服务器开小差，请稍后重试！";
        } else {
            if (ex instanceof AgileBaseException) {
                msg = ex.getMessage();
            }
        }
        return msg;
    }

    /**
     * 处理dubbo超时异常信息
     */
    private static int handlerTimeOutException(Exception ex) {
        String exceptionMessage = ex.getMessage() == null ? String.valueOf(ex) : ex.getMessage();
        return exceptionMessage.indexOf("times of the providers");
    }
}
