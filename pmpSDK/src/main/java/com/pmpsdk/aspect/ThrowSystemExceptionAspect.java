package com.pmpsdk.aspect;

import com.pmpsdk.annotation.ThrowSystemException;
import com.pmpsdk.exception.SystemException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ThrowSystemExceptionAspect {

    /**
     * 拦截带有 @ThrowSystemException 注解的方法
     * 捕获异常并转换为 SystemException
     */
    @Around("@annotation(throwSystemException)")
    public Object handleSystemException(ProceedingJoinPoint joinPoint
            , ThrowSystemException throwSystemException) throws Throwable {
        try {
            return joinPoint.proceed(); // 执行原方法
        } catch (Exception ex) {
            // 如果方法抛出异常，则转换为 SystemException 并携带原始异常堆栈
            throw new SystemException(throwSystemException.code()
                    , throwSystemException.message(), ex);
        }
    }
}