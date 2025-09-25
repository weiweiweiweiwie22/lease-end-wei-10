package com.weiwei.lease.common.exception;

import com.weiwei.lease.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.Getter;

@Data // lombok注解，自动生成getter方法，方便获取code
public class BusinessException extends RuntimeException {

    private final Integer code; // 业务状态码

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}