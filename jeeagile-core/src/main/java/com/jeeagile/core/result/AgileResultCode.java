package com.jeeagile.core.result;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 系统默认请求响应编码枚举
 */
public enum AgileResultCode implements IAgileResultCode {
    SUCCESS("0000", "成功"),

    FAIL_AUTH("1000", "用户认证失败！"),
    FAIL_USER_INFO("1001", "用户登录信息失效或未进行登录!"),
    FAIL_USER_PERMS("1002", "用户访问权限不足!"),
    FAIL_USER_NAME("1003", "用户名错误！"),
    FAIL_USER_PWD("1004", "用户密码错误！"),
    FAIL_USER_DISABLE("1005", "用户已被停用!"),
    FAIL_USER_FREEZE("1006", "用户已被冻结!"),
    FAIL_KCAPTCHA_EXPIRE("1007", "验证码已失效!"),
    FAIL_KCAPTCHA_ERROR("1008", "验证码错误!"),

    FAIL_OPS("2000", "业务操作失败！"),
    FAIL_OPS_SAVE("2001", "保存操作失败！"),
    FAIL_OPS_DELETE("2002", "删除操作失败！"),
    FAIL_OPS_UPDATE("2003", "更新操作失败！"),
    FAIL_OPS_SEARCH("2004", "查询操作失败！"),
    FAIL_OPS_IMPORT("2005", "导入操作失败！"),
    FAIL_OPS_EXPORT("2006", "导出操作失败！"),

    WARN_OPS("3000", "业务操作警告！"),
    WARN_SEARCH_NO_RESULT("3001", "查询无结果集！"),
    WARN_SEARCH_LARGER_RESULT("3002", "查询结果集过大！"),
    WARN_VALIDATE_PASSED("3003", "数据验证未通过！"),


    FAIL_FILE_OPS("4000", "文件操作失败！"),
    FAIL_FILE_UPLOAD("4001", "文件上传失败！"),
    FAIL_FILE_DOWNLOAD("4002", "文件下载失败！"),


    FAIL_SERVER_EXCEPTION("9000", "服务器内部异常！"),
    FAIL_SAVE_EXCEPTION("9001", "保存异常！"),
    FAIL_DELETE_EXCEPTION("9002", "删除异常！"),
    FAIL_UPDATE_EXCEPTION("9003", "更新异常！"),
    FAIL_SEARCH_EXCEPTION("9004", "查询异常！"),
    FAIL_IMPORT_EXCEPTION("9005", "导入异常！"),
    FAIL_KCAPTCHA_EXCEPTION("9006", "验证码生成异常!"),
    FAIL_AUTH_EXCEPTION("9007", "认证异常！"),
    FAIL_IO_EXCEPTION("9008", "IO操作异常！"),
    FAIL_CRYPTO_EXCEPTION("9009", "加解密异常！"),
    FAIL_HTTP_EXCEPTION("9010", "HTTP请求异常！"),
    FAIL_TIMEOUT_EXCEPTION("9011", "服务请求超时异常！"),
    FAIL_RABBIT_EXCEPTION("9012", "Rabbit异常！"),
    FAIL_DEMO_EXCEPTION("9099", "演示模式，禁止操作！");


    /**
     * 编码
     */
    private String code;
    /**
     * 描述
     */
    private String message;

    /**
     * 构造函数
     *
     * @param code
     * @param message
     */
    AgileResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
