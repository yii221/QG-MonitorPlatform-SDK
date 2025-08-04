package com.pmpsdk.exception;


import com.pmpsdk.domain.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.pmpsdk.domain.Code.INTERNAL_ERROR;

@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(SystemException.class)
    public void doSystemException(SystemException ex) {
        System.out.println("==>\n系统异常:  "
                + ex.getCode() + "\n" + ex.getMessage() + "\n<==");


        // 继续放行，返回原始异常信息
        throw ex;
        // return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public void doBusinessException(BusinessException ex) {
        System.out.println("==>\n业务异常: "
                + ex.getCode() + "\n" + ex.getMessage() + "\n<==");


        // 继续放行，返回原始异常信息
        throw ex;
        // return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void doOtherException(Exception ex) throws Exception {
        System.out.println("==>\n未知异常:\n" + ex.getMessage() + "\n<==");


        // 继续放行，返回原始异常信息
        throw ex;
        // return new Result(INTERNAL_ERROR, "系统繁忙，请稍后再试！");
    }

}