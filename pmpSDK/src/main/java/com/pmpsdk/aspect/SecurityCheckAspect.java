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


    @Around("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public Object checkSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
        // 在这里进行安全检查
        // 获取请求信息

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = GetClientIpUtil.getClientIp(request);

        // TODO: 检测 ip是否在黑名单中
        if (shouldIntercept(ip)) {
            System.err.println("\n\n=======ip在黑名单中********\n\n");
            LogUtil.warn("拦截IP: {}", ip);
            return "访问被拒绝：IP已被列入黑名单";
        }

        String userAgent = request.getHeader("User-Agent");
        EnvironmentSnapshot environmentSnapshot = UserAgentUtil.parseUserAgent(userAgent);
//        // 简单示例：判断IP是否在黑名单
//        if (/*isBlackIp(ip)*/ true) {
//            LogUtil.warn("检测到黑名单IP访问: " + ip + ", userAgent: "
//                    + userAgent + "\n解析处理后信息：" + environmentSnapshot + "\n\n");
//        }
        // 如果检查通过，继续执行目标方法
        return joinPoint.proceed();
    }
}
