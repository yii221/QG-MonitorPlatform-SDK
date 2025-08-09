package com.pmpsdk.aspect;

import com.pmpsdk.exception.SDKException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)   // 1、优先把SDK内部异常转换
public class SDKExceptionAspect {

    // 切面访问：仅SDK内部
    @Around("execution(* com.pmpsdk..*.*(..))")
    public Object convertException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 将异常转换为SDKException抛出
            throw new SDKException(500, "SDK执行异常", e);
        }
    }
}
