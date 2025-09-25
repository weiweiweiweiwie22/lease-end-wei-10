package com.weiwei.lease.common.exception;

import com.weiwei.lease.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handleBusinessException(BusinessException e) {
        // 打印日志，方便追踪
        System.err.println("业务异常: " + e.getMessage());
        // 返回 BusinessException 中携带的具体错误码和信息
        return Result.fail(e.getCode(),e.getMessage());
    }
}