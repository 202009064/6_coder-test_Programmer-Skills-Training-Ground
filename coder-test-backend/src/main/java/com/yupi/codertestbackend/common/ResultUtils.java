package com.yupi.codertestbackend.common;

/**
 * 响应工具类
 */
public class ResultUtils {

    private ResultUtils() {
    }

    /**
     * 成功（无数据）
     */
    public static BaseResponse<Void> success() {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 成功（有数据）
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 失败
     */
    public static BaseResponse<Void> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败（自定义消息）
     */
    public static BaseResponse<Void> error(int code, String message) {
        return new BaseResponse<>(code, message);
    }

    /**
     * 失败
     */
    public static BaseResponse<Void> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), message);
    }
}
