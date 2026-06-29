package com.example.campusgrade.common;

/**
 * 所有接口统一使用的响应包装类。
 *
 * <p>泛型 {@code T} 表示 data 中实际存放的数据类型，例如用户对象或成绩列表。
 * 统一结构便于前端始终通过 code、message 和 data 三个字段处理响应。</p>
 *
 * @param <T> 响应数据的类型
 */
public class ApiResponse<T> {
    /** 业务状态码，当前成功时使用 200。 */
    private int code;
    /** 给前端或用户阅读的提示信息。 */
    private String message;
    /** 接口实际返回的数据；失败时通常为 null。 */
    private T data;

    /** 无参构造方法，供 JSON 序列化框架创建对象。 */
    public ApiResponse() {
    }

    /** 创建一个包含完整响应信息的对象。 */
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 快速构造成功响应。
     *
     * @param data 要返回给前端的数据
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    /**
     * 快速构造失败响应。
     *
     * @param message 失败原因
     */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(500, message, null);
    }

    /** 获取业务状态码。 */
    public int getCode() {
        return code;
    }

    /** 设置业务状态码。 */
    public void setCode(int code) {
        this.code = code;
    }

    /** 获取提示信息。 */
    public String getMessage() {
        return message;
    }

    /** 设置提示信息。 */
    public void setMessage(String message) {
        this.message = message;
    }

    /** 获取实际响应数据。 */
    public T getData() {
        return data;
    }

    /** 设置实际响应数据。 */
    public void setData(T data) {
        this.data = data;
    }
}
