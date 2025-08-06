package com.pmpsdk.exception;

import com.pmpsdk.annotation.ThrowSDKException;

@ThrowSDKException
public class SDKException extends RuntimeException{

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public SDKException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public SDKException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }



}
