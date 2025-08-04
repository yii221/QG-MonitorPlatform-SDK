package com.pmpsdk.exception;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import com.pmpsdk.annotation.Model;
import com.pmpsdk.annotation.Monitor;
import com.pmpsdk.domain.ErrorMessage;
import com.pmpsdk.domain.Message;
import com.pmpsdk.domain.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;

import static com.pmpsdk.domain.Code.INTERNAL_ERROR;

@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(SystemException.class)
    public void doSystemException(SystemException ex) {
        System.out.println("==>\n系统异常:  "
                + ex.getCode() + "\n" + ex.getMessage() + "\n<==");



        // 继续放行，返回原始异常信息
      //throw ex;
        // return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public void doBusinessException(BusinessException ex) {
        System.out.println("==>\n业务异常: "
                + ex.getCode() + "\n" + ex.getMessage() + "\n<==");


        // 继续放行，返回原始异常信息
       //throw ex;
        // return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void doOtherException(Exception ex) throws Exception {
        System.out.println("==>\n未知异常:\n" + ex.getMessage() + "\n<==");
        // 获取异常类型
        Class<?> exceptionClass = ex.getClass();
        System.out.println("异常类型: " + exceptionClass.getName());

        // 获取异常发生的类和方法（堆栈第一个元素通常是出错点）
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 0) {
            String errorClass = stackTrace[0].getClassName();
            String errorMethod = stackTrace[0].getMethodName();
            int lineNumber = stackTrace[0].getLineNumber();
            System.out.println("出错类: " + errorClass);
            System.out.println("出错方法: " + errorMethod);
            System.out.println("出错行号: " + lineNumber);

            ErrorMessage message = new ErrorMessage();

            message.setStack("出错类: " + errorClass + "\n" +
                    "出错方法: " + errorMethod + "\n" +
                    "出错行号: " + lineNumber + "\n" +
                    "异常信息: " + ex.getMessage());

            message.setType("OtherException");
            message.setTimestamp(System.currentTimeMillis());
           // message.setLevel("ERROR");

            Class<?> clazz = Class.forName(errorClass);
            Model modelAnnotation = clazz.getAnnotation(Model.class);

            if (modelAnnotation != null) {
                String type = modelAnnotation.type();
                message.setModel(type);
                System.out.println("Model注解类型: " + type);
            } else {
                System.out.println("没有Model注解");
            }

            // 获取方法上的注解
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(errorMethod)) {
                    if (method.isAnnotationPresent(Monitor.class)) {
                        sendMessage(message);
                    }
                }
            }
        }

        // 继续放行，返回原始异常信息
        //throw ex;
        // return new Result(INTERNAL_ERROR, "系统繁忙，请稍后再试！");
    }



    private void sendMessage(ErrorMessage message) {
        // 这里可以实现发送消息的逻辑，比如通过消息队列、日志系统等
        System.out.println("发送消息: " + message);

        JSON json = cn.hutool.json.JSONUtil.parse(message);
        System.out.println("JSON格式的消息: " + json.toString());

        HttpUtil.post("http://localhost:8080/api/error", json.toString());
    }

}