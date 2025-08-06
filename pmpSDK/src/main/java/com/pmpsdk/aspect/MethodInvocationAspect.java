package com.pmpsdk.aspect;

import com.pmpsdk.utils.IpBlacklistUtil;
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

    // TODO: 定时，每10秒打印方法调用统计
    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            // TODO: 清理空队列
            methodInvocationWindows
                    .entrySet()
                    .removeIf(entry -> entry.getValue().isEmpty());

            Map<String, Integer> methodStats = getMethodInvocationStatistics();
            if (!methodStats.isEmpty()) {
                try {
                    methodInvocationWindows.forEach((methodWithIp, queue) -> {
                        // 解析方法名和IP（格式：methodName@IP）
                        String[] parts = methodWithIp.split("@", 2);
                        String methodName = parts[0];
                        String ip = parts.length > 1 ? parts[1] : "N/A";
                    });
                } catch (Exception e) {
                    // 异常处理
                }
            }
        }, 1, 10, TimeUnit.SECONDS);
    }


    @Around("execution(* com.*..*.*(..)) && !within(com.pmpsdk..*)")
    public Object countMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        // TODO: 获取方法名
        String methodName = joinPoint.getSignature().toShortString();

        // TODO: 方法调用次数统计
        methodInvocationCounts
                .computeIfAbsent(methodName, k -> new AtomicInteger(0))
                .incrementAndGet();

        // TODO: 恶意攻击检测
        try {
            // TODO: 获取当前请求
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            // TODO: 获取客户端IP
            String clientIp = getClientIp(request);
            String methodWithIp = methodName + "@" + clientIp;

            // TODO: 判断是否为恶意攻击
            if (isMaliciousAttack(methodWithIp)) {
                // 可以选择抛出异常来阻止请求
                // throw new SecurityException("请求频率过高");
                // TODO: 加入黑名单
                IpBlacklistUtil.addToBlacklist(clientIp);
            }
        } catch (IllegalStateException e) {
            // TODO: 非Web请求（如定时任务）跳过检测
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
        // TODO: 获取或创建时间戳队列
        Queue<Long> windowQueue = methodInvocationWindows.computeIfAbsent(
                methodWithIp, k -> new ConcurrentLinkedQueue<>()
        );

        synchronized (windowQueue) {
            // TODO: 移除过期的窗口
            while (!windowQueue.isEmpty() && (currentTime - windowQueue.peek()) > WINDOW_TIME_MS) {
                windowQueue.poll();
            }

            // TODO: 添加当前窗口时间戳
            windowQueue.add(currentTime);


            // TODO: 判断是否超过恶意调用阈值
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
