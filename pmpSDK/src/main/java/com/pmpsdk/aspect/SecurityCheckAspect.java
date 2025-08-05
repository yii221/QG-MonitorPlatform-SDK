package com.pmpsdk.aspect;

import com.pmpsdk.log.LogUtils;
import com.pmpsdk.utils.GetClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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



    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object checkSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
        // 在这里进行安全检查
        // 获取请求信息

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = GetClientIpUtil.getClientIp(request);

        String userAgent = request.getHeader("User-Agent");
        System.out.println("IP: " + ip + ", User-Agent: " + userAgent);
        // 简单示例：判断IP是否在黑名单
        if (/*isBlackIp(ip)*/ true) {
            LogUtils.warn("检测到黑名单IP访问: " + ip + ", UA: " + userAgent);
        }
        // 如果检查通过，继续执行目标方法
        return joinPoint.proceed();
    }
}
