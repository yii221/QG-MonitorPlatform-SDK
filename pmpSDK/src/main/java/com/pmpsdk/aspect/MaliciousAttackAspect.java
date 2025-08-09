package com.pmpsdk.aspect;

import cn.hutool.json.JSONUtil;
import com.pmpsdk.domain.Result;
import com.pmpsdk.utils.IpBlacklistUtil;
import com.pmpsdk.utils.LogUtil;
import com.pmpsdk.utils.SpringContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.pmpsdk.utils.GetClientIpUtil.getClientIp;

@Aspect
@Component
@Order(2)       // TODO: 3、检测是否有恶意访问攻击
@DependsOn("springContextUtil")
public class MaliciousAttackAspect {

    private static final ConcurrentHashMap<String, Queue<Long>> methodInvocationWindows = new ConcurrentHashMap<>();

    /**
     * 恶意调用阈值
     * 默认1秒内调用3次
     */
    private static final long WINDOW_TIME_MS = 1000;
    // TODO: 获取恶意调用阈值
    private static final int MALICIOUS_THRESHOLD = SpringContextUtil.MALICIOUS_THRESHOLD;

    static {
        // TODO: 定时，清理空滑动窗口队列
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            methodInvocationWindows
                    .entrySet()
                    .removeIf(entry -> entry.getValue().isEmpty());
            // TODO: 1秒后，每10秒执行1次
        }, 1, 10, TimeUnit.SECONDS);
    }

    /**
     * 侦察非法攻击
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    // TODO：切面范围：除了SDK内部的 @Controller和 @RestController
    @Around("(@within(org.springframework.web.bind.annotation.RestController)" +
            " || @within(org.springframework.stereotype.Controller))" +
            " && !within(com.pmpsdk..*)")
    public Object detectMaliciousAttack(ProceedingJoinPoint joinPoint) throws Throwable {
       // TODO: 恶意攻击检测
        try {
            // TODO: 获取当前请求
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            // TODO: 获取客户端IP
            String clientIp = getClientIp(request);
            // TODO: 获取方法名
            String methodName = joinPoint.getSignature().toShortString();
            // TODO: 获取方法名与IP的组合
            String methodWithIp = methodName + "@" + clientIp;

            // TODO: 判断是否为恶意攻击
            if (isMaliciousAttack(methodWithIp)) {
                // TODO: 加入黑名单
                IpBlacklistUtil.addToBlacklist(clientIp);
                LogUtil.warn("IP: " + clientIp + "，访问过频繁，已拉入黑名单");
                return JSONUtil.toJsonStr(new Result(403, "访问被拒绝：IP已被列入黑名单"));
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
}