package com.pmpsdk.aspect;


import com.pmpsdk.annotation.ThrowSDKException;
import com.pmpsdk.exception.SDKException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@ThrowSDKException
@Aspect
@Component
public class SDKExceptionAspect {

    @Around("@within(com.pmpsdk.annotation.ThrowSDKException)" +
            " || @annotation(com.pmpsdk.annotation.ThrowSDKException)")
    public Object convertException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 将异常转换为SDKException抛出
            throw new SDKException(500, "SDK执行异常", e);
        }
    }
}
