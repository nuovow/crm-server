package com.nuo.crmserver.exceptions;

import com.nuo.crmserver.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions  {
    /**
     * 自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(BizException.class)
    public Result<String> handelBizException(BizException e){
        return Result.error(e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handelException(RuntimeException e){
        return Result.error(e.getMessage());
    }
}
