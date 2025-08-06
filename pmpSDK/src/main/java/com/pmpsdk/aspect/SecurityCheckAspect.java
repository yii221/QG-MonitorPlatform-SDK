package com.pmpsdk.aspect;

import com.pmpsdk.domain.EnvironmentSnapshot;

import com.pmpsdk.utils.LogUtil;
import com.pmpsdk.utils.GetClientIpUtil;

import com.pmpsdk.utils.UserAgentUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.pmpsdk.utils.GetClientIpUtil.shouldIntercept;

/**
 * @Description: 校验访问  // 类说明
 * @ClassName: SecurityCheckAspect    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/5 14:57   // 时间
 * @Version: 1.0     // 版本
 */
@Aspect
@Component
@Order(0)
public class SecurityCheckAspect {

    /**
     * 通过 ip，绑定环境快照
     */
    public static final ConcurrentHashMap<String, EnvironmentSnapshot> environmentSnapshot = new ConcurrentHashMap<>();

    // TODO: 定时，每分钟清空一次环境快照
    static {
        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(environmentSnapshot::clear, 0, 1, TimeUnit.MINUTES);
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public Object checkSecurity(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = GetClientIpUtil.getClientIp(request);

        // TODO: 检测 ip是否在黑名单中
        if (shouldIntercept(ip)) {
            LogUtil.warn("拦截IP: {}", ip);
            return "访问被拒绝：IP已被列入黑名单";
        }

        String userAgent = request.getHeader("User-Agent");
        String protocol = request.getScheme();
        String httpMethod = request.getMethod();

        // TODO：记录网络信息
        EnvironmentSnapshot snapshot = UserAgentUtil.parseUserAgent(userAgent);
        snapshot.setIp(ip);
        snapshot.setProtocol(protocol);
        snapshot.setHttpMethod(httpMethod);

        // TODO：获取语言
        snapshot.setLanguage(request.getHeader("Accept-Language"));
        snapshot.setIsAjax("XMLHttpRequest".equals(request.getHeader("X-Requested-With")));

        // TODO：放进 map集合
        environmentSnapshot.putIfAbsent(ip, snapshot);

        // TODO：继续执行目标方法
        return joinPoint.proceed();
    }
}
