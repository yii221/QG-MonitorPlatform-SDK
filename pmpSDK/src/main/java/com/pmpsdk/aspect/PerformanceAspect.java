package com.pmpsdk.aspect;

import com.pmpsdk.annotation.Module;
import com.pmpsdk.annotation.ThrowSDKException;
import com.pmpsdk.client.QGAPIClient;
import com.pmpsdk.domain.PerformanceLog;
import com.pmpsdk.utils.LogUtil;
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
@ThrowSDKException
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
                    LogUtil.info("批量性能日志上报: " + batch.size() + " 条");
                } catch (Exception e) {
                    LogUtil.error("批量性能日志上报异常" + e.getMessage());
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }


    @Around("(@annotation(com.pmpsdk.annotation.Monitor))&&(@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller))")
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


            String methodName = pjp.getSignature().toShortString();

            // 上报性能日志
            LogUtil.info("被监测的方法：" + methodName +
                    "\n性能检测到：API: " + log.getApi() +
                    ", \nDuration: " + log.getDuration() + "ms" +
                    ", \nModule: " + log.getModule() +
                    ", \nProject ID: " + log.getProjectId() +
                    ", \nSlow: " + log.isSlow()
                    + ", \nEnvironment: " + log.getEnvironment());
            logQueue.add(log);

        }

    }
}
