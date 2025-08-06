package com.pmpsdk.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pmpsdk.utils.GetClientIpUtil.getClientIp;

@Aspect
@Component
public class MethodInvocationAspect {
    private static final ConcurrentHashMap<String, AtomicInteger> methodInvocationCounts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Queue<Long>> methodInvocationWindows = new ConcurrentHashMap<>();
    /**
     * 恶意调用阈值
     * 1秒内
     * 调用20次
     */
    private static final long WINDOW_TIME_MS = 1000;
    private static final int MALICIOUS_THRESHOLD = 20;

    /**
     * 定时，每10秒打印方法调用统计
     */
    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            /** 清理空队列 **/
            methodInvocationWindows
                    .entrySet()
                    .removeIf(entry -> entry.getValue().isEmpty());

            Map<String, Integer> methodStats = getMethodInvocationStatistics();
            if (!methodStats.isEmpty()) {
                try {
                    System.out.println("\n========== 方法+IP调用统计 ==========");
                    methodInvocationWindows.forEach((methodWithIp, queue) -> {
                        // 解析方法名和IP（格式：methodName@IP）
                        String[] parts = methodWithIp.split("@", 2);
                        String methodName = parts[0];
                        String ip = parts.length > 1 ? parts[1] : "N/A";

                        System.out.printf(
                                "方法: %-40s | IP: %-15s | 最近1分钟调用次数: %d\n",
                                methodName, ip, queue.size()
                        );
                    });
                    System.out.println("======================================");
                } catch (Exception e) {
                    // 异常处理
                }
            }
        }, 1, 10, TimeUnit.SECONDS);
    }


    // 👇最后再放出来
    // @Around("execution(* com.*..*.*(..)) && !within(com.pmpsdk..*)")
    @Around("within(com.pmpsdk.controller..*) || " +
            "within(com.pmpsdk.service..*) || " +
            "within(com.pmpsdk.mapper..*) || " +
            "execution(* com.pmpsdk.exception.ProjectExceptionAdvice.doOtherException(..))")
    public Object countMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        /** 获取方法名 **/
        String methodName = joinPoint.getSignature().toShortString();

        /** 方法调用次数统计 **/
        methodInvocationCounts
                .computeIfAbsent(methodName, k -> new AtomicInteger(0))
                .incrementAndGet();

        /** 恶意攻击检测 **/
        try {
            /** 获取当前请求 **/
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            /** 获取客户端IP **/
            String clientIp = getClientIp(request);
            String methodWithIp = methodName + "@" + clientIp;

            /** 判断是否为恶意攻击 **/
            if (isMaliciousAttack(methodWithIp)) {
                System.err.println("\n\n====>恶意攻击检测: " + methodWithIp + "\n\n");
                // 可以选择抛出异常来阻止请求
                // throw new SecurityException("请求频率过高");
            }
        } catch (IllegalStateException e) {
            /** 非Web请求（如定时任务）跳过检测 **/
            System.out.println("非Web请求，跳过恶意攻击检测");
        }

        return joinPoint.proceed();
    }

    /**
     * 判断是否为非法攻击
     *
     * @param methodWithIp
     * @return
     */
    private boolean isMaliciousAttack(String methodWithIp) {
        long currentTime = System.currentTimeMillis();
        /** 获取或创建时间戳队列 **/
        Queue<Long> windowQueue = methodInvocationWindows.computeIfAbsent(
                methodWithIp, k -> new ConcurrentLinkedQueue<>()
        );

        synchronized (windowQueue) {
            /** 移除过期的窗口 **/
            while (!windowQueue.isEmpty() && (currentTime - windowQueue.peek()) > WINDOW_TIME_MS) {
                windowQueue.poll();
            }

            /** 添加当前窗口时间戳 **/
            windowQueue.add(currentTime);


            /** 判断是否超过恶意调用阈值 **/
            return windowQueue.size() >= MALICIOUS_THRESHOLD;
        }

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
