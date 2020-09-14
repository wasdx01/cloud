package com.bochao.common.config;

import com.bochao.common.exception.BcException;
import com.bochao.common.pojo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常捕获
 * @author 陈晓峰
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理验证信息返回
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return Result.failResponse(null,500,fieldError.getDefaultMessage());
    }

    /**
     * 处理自定义异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BcException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleOrderException(BcException ex) {
        return Result.failResponse(null,ex.getCode()==null?500:ex.getCode(),ex.getMessage());
    }
}