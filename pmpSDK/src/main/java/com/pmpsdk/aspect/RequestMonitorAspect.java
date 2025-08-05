package com.pmpsdk.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.pmpsdk.annotation.Monitor;
/**
 * @Description: 监控AOP  // 类说明
 * @ClassName: RequestMonitorAspect    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/5 10:14   // 时间
 * @Version: 1.0     // 版本
 */
@Aspect
@Component
public class RequestMonitorAspect {

    @Around("@annotation(com.pmpsdk.annotation.Monitor)")
    public Object monitorRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            String methodName = joinPoint.getSignature().toShortString();
            System.out.println("Method " + methodName + " executed in " + duration + " ms");
        }
    }


    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object monitorRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            String methodName = joinPoint.getSignature().toShortString();
            System.out.println("REST Controller Method " + methodName + " executed in " + duration + " ms");
        }
    }
}
