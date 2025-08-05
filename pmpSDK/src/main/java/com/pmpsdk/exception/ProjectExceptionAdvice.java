package com.pmpsdk.exception;


import cn.hutool.json.JSON;
import com.pmpsdk.annotation.Model;
import com.pmpsdk.annotation.Monitor;
import com.pmpsdk.client.QGAPIClient;
import com.pmpsdk.domain.ErrorMessage;
import com.pmpsdk.log.LogUtils;
import com.pmpsdk.utils.PostToServer;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;

@RestControllerAdvice
public class ProjectExceptionAdvice {

    @Resource
    private QGAPIClient qgAPIClient;

    @ExceptionHandler(SystemException.class)
    public void doSystemException(SystemException ex) throws ClassNotFoundException {
        System.out.println("==>\n系统异常:  "
                + ex.getCode() + "\n" + ex.getMessage() + "\n<==");

        errorMethod(ex);
    }

    @ExceptionHandler(BusinessException.class)
    public void doBusinessException(BusinessException ex) throws ClassNotFoundException {
        System.out.println("==>\n业务异常: "
                + ex.getCode() + "\n" + ex.getMessage() + "\n<==");
        errorMethod(ex);
    }

    @ExceptionHandler(Exception.class)
    public void doOtherException(Exception ex) throws Exception {
        System.out.println("==>\n未知异常:\n" + ex.getMessage() + "\n<==");

        System.out.println("111");
        errorMethod(ex);
        System.out.println("222");
    }



    private void errorMethod(Exception ex) throws ClassNotFoundException {

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

            message.setProjectId(qgAPIClient.getProjectToken());



            // 获取方法上的注解
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(errorMethod)) {
                    System.out.println("出错方法666: " + method.getName());
                    if (method.isAnnotationPresent(Monitor.class)) {
                        System.out.println("方法上有Monitor注解");
                        PostToServer.sendMessage(message);
                        LogUtils.error(message.getStack()+
                                "\n项目ID: " + message.getProjectId() +
                                "\n模型类型: " + message.getModel());
                    }
                }
            }

        }
    }
}

