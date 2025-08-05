package com.pmpsdk.aspect;

import com.pmpsdk.annotation.Module;
import com.pmpsdk.client.QGAPIClient;
import com.pmpsdk.domain.PerformanceLog;
import com.pmpsdk.log.LogUtils;
import com.pmpsdk.utils.PostToServer;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description: 性能监控  // 类说明
 * @ClassName: PerformanceAspect    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/5 15:47   // 时间
 * @Version: 1.0     // 版本
 */
@Aspect
@Component
@Order(1)
public class PerformanceAspect {

    @Resource
    private QGAPIClient qgAPIClient;



    private static final long SLOW_THRESHOLD = 1000; // 慢请求阈值，单位毫秒
    private static final LongAdder qps = new LongAdder();
    private static final Queue<PerformanceLog> logQueue = new ConcurrentLinkedQueue<>();

    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            List<PerformanceLog> batch = new ArrayList<>();
            PerformanceLog log;
            while ((log = logQueue.poll()) != null) {
                batch.add(log);
            }
            if (!batch.isEmpty()) {
                try {
                    // 这里将批量日志发送到服务器
                    PostToServer.sendPerformanceLogMessage(batch);
                    System.out.println("批量性能日志上报: " + batch.size() + " 条");
                } catch (Exception e) {
                    LogUtils.error("批量性能日志上报异常"+e.getMessage());
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }


    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            qps.increment();
            PerformanceLog log = new PerformanceLog();
            log.setApi(pjp.getSignature().toShortString());
            log.setDuration(duration);
            log.setTimestamp(System.currentTimeMillis());
            log.setSlow(duration > SLOW_THRESHOLD);

            Class<?> targetClass = pjp.getTarget().getClass();
            String module = "UnknownModel";
            if (targetClass.isAnnotationPresent(Module.class)) {
                module = targetClass.getAnnotation(Module.class).type();
            }
            log.setModule(module);


            log.setProjectId(qgAPIClient.getProjectToken());
            log.setEnvironment(qgAPIClient.getEnvironment());


            System.out.println("API: " + log.getApi() +
                    ", Duration: " + log.getDuration() + "ms" +
                    ", Module: " + log.getModule() +
                    ", Project ID: " + log.getProjectId() +
                    ", Slow: " + log.isSlow()
                    + ", Environment: " + log.getEnvironment());
            // 上报性能日志

            logQueue.add(log);

        }

    }
}
