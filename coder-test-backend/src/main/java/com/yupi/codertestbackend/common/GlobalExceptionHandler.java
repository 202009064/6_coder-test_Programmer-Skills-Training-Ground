package com.yupi.codertestbackend.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Void> handleRuntimeException(RuntimeException e) {
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}
