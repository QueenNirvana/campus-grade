package com.example.campusgrade.common;

/**
 * 表示可预期的业务错误，例如“课程不存在”或“教师无权操作该课程”。
 *
 * <p>继承 RuntimeException 后，业务代码可以直接抛出异常，
 * 再由 {@link GlobalExceptionHandler} 统一转换为接口响应。</p>
 */
public class BusinessException extends RuntimeException {
    /** 使用面向用户的错误信息创建业务异常。 */
    public BusinessException(String message) {
        super(message);
    }
}
