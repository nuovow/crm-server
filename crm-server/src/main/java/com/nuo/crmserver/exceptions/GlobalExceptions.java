package com.nuo.crmserver.exceptions;

import com.nuo.crmserver.common.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {

    /**
     * 业务异常：预期内的失败，message 给用户看
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        return Result.error(e.getMessage());
    }

    /**
     * 参数校验失败：@Valid 校验不通过时抛出，取第一条提示给用户
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null ? "参数错误" : fieldError.getDefaultMessage();
        return Result.error(message);
    }

    /**
     * 未知异常兜底：message 不给用户，堆栈进日志
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleException(RuntimeException e) {
        e.printStackTrace();
        return Result.error("系统繁忙，请稍后重试");
    }
}
