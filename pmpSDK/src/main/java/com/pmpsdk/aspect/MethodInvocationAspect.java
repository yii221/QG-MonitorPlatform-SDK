package com.pmpsdk.aspect;

import com.pmpsdk.utils.LogUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pmpsdk.utils.PostToServer.sendMethodInvocationStats;


@Aspect
@Component
@Order(5)       // TODO：6、统计方法调用次数
public class MethodInvocationAspect {

    private static final ConcurrentHashMap<String, AtomicInteger> methodInvocationCounts = new ConcurrentHashMap<>();

    static {
        // TODO: 定时，发送方法调用情况到服务端，并清空
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            // TODO: 获取所有方法的调用统计
            Map<String, Integer> statistics = getMethodInvocationStatistics();
            if (!statistics.isEmpty()) {
                try {
                    // TODO: 发送到服务器（需实现PostToServer）
                    sendMethodInvocationStats(statistics);
                    LogUtil.info("方法统计上报", "SDK");
                    // TODO: 清空统计
                    methodInvocationCounts.clear();
                } catch (Exception e) {
                    LogUtil.error("服务器繁忙: " + statistics.size() + " 条记录");
                }
            }
            // TODO: 1分钟后，每小时执行1次
        }, 5, 10, TimeUnit.SECONDS);
    }

    /**
     * 统计方法调用次数
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    // TODO：切面范围：@MethodInvocation注解下的类或方法
    @Around("@within(com.pmpsdk.annotation.MethodInvocation) || " +
            "@annotation(com.pmpsdk.annotation.MethodInvocation)")
    public Object countMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        // TODO: 获取方法名
        String methodName = joinPoint.getSignature().toShortString();

        // TODO: 方法调用次数统计
        methodInvocationCounts
                .computeIfAbsent(methodName, k -> new AtomicInteger(0))
                .incrementAndGet();

        return joinPoint.proceed();
    }


    /**
     * 获取所有方法的调用统计
     *
     * @return
     */
    public static Map<String, Integer> getMethodInvocationStatistics() {
        Map<String, Integer> result = new HashMap<>();
        methodInvocationCounts.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }

    /**
     * 获取某个方法的调用次数
     *
     * @param methodName
     * @return
     */
    public int getMethodInvocationCount(String methodName) {
        return methodInvocationCounts.getOrDefault(methodName, new AtomicInteger(0)).get();
    }
}
