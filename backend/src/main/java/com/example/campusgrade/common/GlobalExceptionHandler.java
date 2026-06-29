package com.example.campusgrade.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，将控制器中抛出的异常转换成统一的 {@link ApiResponse}。
 *
 * <p>{@code @RestControllerAdvice} 会作用于所有 REST 控制器，
 * 避免每个接口重复编写 try/catch。</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /** 将业务异常转换为 HTTP 400，错误信息直接使用业务层给出的原因。 */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusiness(BusinessException ex) {
        return ApiResponse.fail(ex.getMessage());
    }

    /** 处理 @Valid 参数校验失败，并优先返回第一个字段的校验提示。 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidation(MethodArgumentNotValidException ex) {
        // 如果框架没有提供具体字段错误，则返回通用提示。
        String message = ex.getBindingResult().getFieldErrors().isEmpty()
                ? "参数校验失败"
                : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ApiResponse.fail(message);
    }

    /** 捕获未单独处理的异常，避免服务器直接返回默认错误页面。 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleOther(Exception ex) {
        return ApiResponse.fail(ex.getMessage());
    }
}
