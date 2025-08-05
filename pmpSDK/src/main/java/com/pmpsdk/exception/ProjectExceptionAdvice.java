package com.pmpsdk.exception;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import com.pmpsdk.QGAPIClientConfig;
import com.pmpsdk.annotation.Model;
import com.pmpsdk.annotation.Monitor;
import com.pmpsdk.client.QGAPIClient;
import com.pmpsdk.domain.ErrorMessage;
import com.pmpsdk.domain.Message;
import com.pmpsdk.domain.Result;
import com.pmpsdk.log.LogUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;

import static com.pmpsdk.domain.Code.INTERNAL_ERROR;

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


    private void sendMessage(ErrorMessage message) {
        // 这里可以实现发送消息的逻辑，比如通过消息队列、日志系统等
        System.out.println("发送消息: " + message);

        JSON json = cn.hutool.json.JSONUtil.parse(message);
        System.out.println("JSON格式的消息: " + json.toString());

        String respond = HttpUtil.post("http://192.168.1.233:8080/messages/error", json.toString());

        System.out.println("响应结果: " + respond);
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
                        sendMessage(message);
                        LogUtils.error(message.getStack()+
                                "\n项目ID: " + message.getProjectId() +
                                "\n模型类型: " + message.getModel());
                    }
                }
            }

        }
    }
}

