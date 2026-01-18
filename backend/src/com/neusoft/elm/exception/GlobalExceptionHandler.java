package com.neusoft.elm.exception;

import com.neusoft.elm.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理器
 * 捕获所有未处理的异常，统一返回 Result 格式，防止前端收到 500 错误页面
 *
 * @author Neusoft ELM Team
 * @version 2.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 SQL 异常
     * 捕获所有 SQLException 及其子类异常
     *
     * @param e SQL 异常
     * @return 统一错误响应
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleSQLException(SQLException e) {
        // 记录异常日志
        System.err.println("❌ 数据库操作异常：" + e.getMessage());
        e.printStackTrace();

        // 返回友好错误信息给前端
        return Result.fail("数据库操作失败：" + e.getMessage());
    }

    /**
     * 处理数字格式异常
     * 例如：解析商家编号时的格式错误
     *
     * @param e 数字格式异常
     * @return 统一错误响应
     */
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleNumberFormatException(NumberFormatException e) {
        System.err.println("❌ 参数格式错误：" + e.getMessage());
        return Result.fail("参数格式错误，请检查输入的数字格式");
    }

    /**
     * 处理空指针异常
     * 通常由于未做 null 检查导致
     *
     * @param e 空指针异常
     * @return 统一错误响应
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        System.err.println("❌ 空指针异常：" + e.getMessage());
        e.printStackTrace();
        return Result.fail("服务器内部错误，请稍后重试");
    }

    /**
     * 处理非法参数异常
     * 例如：参数验证失败
     *
     * @param e 非法参数异常
     * @return 统一错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        System.err.println("❌ 参数验证失败：" + e.getMessage());
        return Result.fail("参数错误：" + e.getMessage());
    }

    /**
     * 处理所有其他未捕获的异常
     * 作为兜底异常处理器
     *
     * @param e 通用异常
     * @return 统一错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleGenericException(Exception e) {
        System.err.println("❌ 未预期的异常：" + e.getClass().getName());
        System.err.println("   异常信息：" + e.getMessage());
        e.printStackTrace();

        // 生产环境应返回通用错误信息，避免泄露敏感信息
        return Result.fail("服务器处理请求时发生错误，请联系管理员");
    }
}
