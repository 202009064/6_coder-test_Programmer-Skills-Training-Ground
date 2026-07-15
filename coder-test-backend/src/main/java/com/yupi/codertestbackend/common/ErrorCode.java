package com.yupi.codertestbackend.common;

/**
 * 错误码枚举
 */
public enum ErrorCode {

    SUCCESS(0, "成功"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN(40100, "未登录"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    USER_EXIST(40001, "账号已存在"),
    USER_NOT_FOUND(40002, "用户不存在"),
    PASSWORD_ERROR(40003, "密码错误"),
    LEVEL_NOT_FOUND(40004, "关卡不存在"),
    AI_ERROR(50001, "AI 服务调用失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
