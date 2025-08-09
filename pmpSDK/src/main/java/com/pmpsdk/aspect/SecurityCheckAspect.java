package com.pmpsdk.aspect;

import cn.hutool.json.JSONUtil;
import com.pmpsdk.domain.EnvironmentSnapshot;
import com.pmpsdk.domain.Result;
import com.pmpsdk.domain.TimedEnvironmentSnapshot;

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
@Order(1)   // 2、然后检验黑名单，通行之后获取环境快照
public class SecurityCheckAspect {

    // 通过 ip，绑定环境快照
    public static final ConcurrentHashMap<String, TimedEnvironmentSnapshot>
            environmentSnapshot = new ConcurrentHashMap<>();

    // 定时，每37秒清空一次环境快照
    static {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() ->
                                environmentSnapshot.entrySet().removeIf(e -> e.getValue().isExpired()),
                        0, 37, TimeUnit.SECONDS
                );
    }

    // 切面范围：所有@RestController、@Controller注解下
    @Around("@within(org.springframework.web.bind.annotation.RestController)" +
            " || @within(org.springframework.stereotype.Controller)")
    public Object checkSecurity(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = GetClientIpUtil.getClientIp(request);

        // 检测 ip是否在黑名单中
        if (shouldIntercept(ip)) {
            return JSONUtil.toJsonStr(new Result(403, "IP地址已被拦截，若有疑问，请联系项目管理员"));
        }

        String userAgent = request.getHeader("User-Agent");
        String protocol = request.getScheme();
        String httpMethod = request.getMethod();

        // 记录网络信息
        EnvironmentSnapshot snapshot = UserAgentUtil.parseUserAgent(userAgent);
        snapshot.setIp(ip);
        snapshot.setProtocol(protocol);
        snapshot.setHttpMethod(httpMethod);

        // 获取语言
        snapshot.setLanguage(request.getHeader("Accept-Language"));
        snapshot.setIsAjax("XMLHttpRequest".equals(request.getHeader("X-Requested-With")));

        // 放进 map集合
        environmentSnapshot.compute(ip, (ipKey, v) -> {
            if (v == null) {
                return new TimedEnvironmentSnapshot(snapshot, 1);
            } else {
                v.resetNewExpireTime(1);
                return v;
            }
        });

        // 继续执行目标方法
        return joinPoint.proceed();
    }
}
