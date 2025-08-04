package com.pmpsdk.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessException extends RuntimeException{
    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
