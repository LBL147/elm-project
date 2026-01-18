package com.neusoft.elm.vo;

/**
 * 统一响应结果封装类
 * 用于对接前端 Axios 的标准响应格式
 *
 * @param <T> 数据类型
 * @author Neusoft ELM Team
 * @version 2.0
 */
public class Result<T> {

    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 私有构造方法，强制使用静态工厂方法创建
     */
    private Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @return Result 对象
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(true, "操作成功", data);
    }

    /**
     * 成功响应（带自定义消息和数据）
     *
     * @param message 成功消息
     * @param data 响应数据
     * @return Result 对象
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(true, message, data);
    }

    /**
     * 成功响应（无数据）
     *
     * @param message 成功消息
     * @return Result 对象
     */
    public static <T> Result<T> ok(String message) {
        return new Result<>(true, message, null);
    }

    /**
     * 失败响应
     *
     * @param message 失败消息
     * @return Result 对象
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(false, message, null);
    }

    // Getter 和 Setter 方法（必须提供，Jackson 序列化需要）

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
