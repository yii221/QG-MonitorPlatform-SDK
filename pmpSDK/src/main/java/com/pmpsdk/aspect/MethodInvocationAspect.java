package com.pmpsdk.aspect;

import com.pmpsdk.domain.PerformanceLog;
import com.pmpsdk.log.LogUtils;
import com.pmpsdk.utils.PostToServer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class MethodInvocationAspect {

    private static final ConcurrentHashMap<String, AtomicInteger> methodInvocationCounts = new ConcurrentHashMap<>();

    // 👇最后再放出来
    // @Around("execution(* com.*..*.*(..)) && !within(com.pmpsdk..*)")
    @Around("within(com.pmpsdk.controller..*) || " +
            "within(com.pmpsdk.service..*) || " +
            "within(com.pmpsdk.mapper..*)")
    public Object countMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        // 原子性增加调用次数
        methodInvocationCounts
                .computeIfAbsent(methodName, k -> new AtomicInteger(0))
                .incrementAndGet();

        return joinPoint.proceed();
    }

    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Map<String, Integer> methodStats = getMethodInvocationStatistics();
            if (!methodStats.isEmpty()) {
                try {
                    System.out.println("=====================================");
                    System.out.println(methodStats);
                    System.out.println("=====================================");
                } catch (Exception e) {

                }
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    /**
     * 获取所有方法的调用统计
     * @return
     */
    public static Map<String, Integer> getMethodInvocationStatistics() {
        Map<String, Integer> result = new HashMap<>();
        methodInvocationCounts.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }

    /**
     * 获取某个方法的调用次数
     * @param methodName
     * @return
     */
    public int getMethodInvocationCount(String methodName) {
        return methodInvocationCounts.getOrDefault(methodName, new AtomicInteger(0)).get();
    }
}