package com.pmpsdk.exception;

import com.pmpsdk.annotation.Module;
import com.pmpsdk.aspect.SecurityCheckAspect;
import com.pmpsdk.client.QGAPIClient;
import com.pmpsdk.domain.EnvironmentSnapshot;
import com.pmpsdk.domain.ErrorMessage;
import com.pmpsdk.domain.Result;
import com.pmpsdk.domain.TimedEnvironmentSnapshot;
import com.pmpsdk.utils.LogUtil;
import com.pmpsdk.utils.PostToServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;


@Order(4)
@RestControllerAdvice
public class ProjectExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(ProjectExceptionAdvice.class);


    @Resource
    private QGAPIClient qgAPIClient;

    /**
     * 处理被注解标记的异常
     *
     * @param ex
     * @throws Exception
     */
    @ExceptionHandler(SDKException.class)
    public Result dealSDKException(SDKException ex) throws Exception {
        errorMethod(ex, 0);
        return new Result(500, "sdk内部异常:" + ex.getMessage());
    }


    /**
     * 处理未被注解标记的异常
     *
     * @param ex
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public Result catchException(Exception ex) throws Exception {
        errorMethod(ex, 1);
        return new Result(500, "捕获到异常:" + ex.getMessage());
    }


    /**
     * 获取异常信息
     *
     * @param ex
     * @throws Exception
     */
    private void errorMethod(Exception ex, int from) throws Exception {

        // 获取环境快照
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        TimedEnvironmentSnapshot timedSnapshot = SecurityCheckAspect.environmentSnapshot.get(ip);
        EnvironmentSnapshot environmentSnapshot = timedSnapshot != null ? timedSnapshot.getSnapshot() : null;

        // 获取异常类型
        Class<?> exceptionClass = ex.getClass();

        // 获取异常发生的类和方法
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 0) {
            String errorClass = stackTrace[0].getClassName();
            String errorMethod = stackTrace[0].getMethodName();
            int lineNumber = stackTrace[0].getLineNumber();

            // 构建异常信息对象
            ErrorMessage message = new ErrorMessage();
            message.setEnvironmentSnapshot(environmentSnapshot);
            message.setErrorType(exceptionClass.getSimpleName());

            message.setStack("出错类: " + errorClass + "\n" +
                    "异常类型: " + exceptionClass.getSimpleName() + "\n" +
                    "出错方法: " + errorMethod + "\n" +
                    "出错行号: " + lineNumber + "\n" +
                    "异常信息: " + ex.getMessage());

            message.setTimestamp(LocalDateTime.now());

            // 获取模块注解内容
            Class<?> clazz = Class.forName(errorClass);
            Module modelAnnotation = clazz.getAnnotation(Module.class);

            if (modelAnnotation != null && modelAnnotation.type() != null) {
                message.setModule(modelAnnotation.type());
            } else {
                message.setModule("unknown");
            }

            // 获取项目Token和运行环境
            message.setProjectId(qgAPIClient.getProjectToken());
            message.setEnvironment(qgAPIClient.getEnvironment());

            if (from == 0) {
                log.error("\n==>\n{}:{}SDK中{}异常:\n{}\n<=="
                        , errorClass, errorMethod, ex.getClass().getName(), ex.getMessage());
            } else {
                log.error("\n==>\n{}:{}捕获到{}异常:\n{}\n<=="
                        , errorClass, errorMethod, ex.getClass().getName(), ex.getMessage());
            }

            // 进行日志记录
            logError(message);

        }

    }

    /**
     * 错误日志记录
     *
     * @param message
     * @throws Exception
     */
    private void logError(ErrorMessage message) throws Exception {
        PostToServer.sendErrorMessage(message);
        LogUtil.error("异常上报=>" + message.getErrorType()
                , message.getModule() != null ? message.getModule() : "unknown");
    }

}

