package com.pmpsdk.aspect;

import com.pmpsdk.annotation.ThrowBusinessException;
import com.pmpsdk.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ThrowBusinessExceptionAspect {

    /**
     * 拦截带有 ThrowBusinessException 注解的方法
     * 捕获异常并转换为 BusinessException
     */
    @Around("@annotation(throwBusinessException)")
    public Object handleBusinessException(ProceedingJoinPoint joinPoint
            , ThrowBusinessException throwBusinessException) throws Throwable {
        try {
            return joinPoint.proceed(); // 执行原方法
        } catch (Exception ex) {
            // 如果方法抛出异常，则转换为 BusinessException 抛出
            throw new BusinessException(throwBusinessException.code()
                    , throwBusinessException.message(), ex);
        }
    }
}
