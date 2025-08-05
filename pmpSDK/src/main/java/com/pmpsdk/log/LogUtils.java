package com.pmpsdk.log;


import com.pmpsdk.QGAPIClientConfig;
import com.pmpsdk.annotation.Model;

import com.pmpsdk.domain.Log;
import com.pmpsdk.utils.PostToServer;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;



public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    private static final LongAdder totalCount = new LongAdder();
    private static final LongAdder errorCount = new LongAdder();
    private static final AtomicLong currentSecondCount = new AtomicLong(0);

    private static final Queue<Log> logQueue = new ConcurrentLinkedQueue<>();

    @Resource
    private static QGAPIClientConfig qgAPIClientConfig;

    static {
        // 定时每秒输出QPS
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            List<Log> batch = new ArrayList<>();
            Log log;
            while ((log = logQueue.poll()) != null) {
                batch.add(log);
            }
            if (!batch.isEmpty()) {
                try {
                    // 这里将批量日志发送到服务器
                    PostToServer.sendLogMessage(batch);
                    System.out.println("批量日志上报: " + batch.size() + " 条");
                } catch (Exception e) {
                    logger.error("批量日志上报异常", e);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    // ------ 日志记录方法（同步本地日志和HTTP上报） ------
    // ------ 日志记录方法 ------
    public static void debug(String message) {
        logger.debug(message);
        totalCount.increment();
        currentSecondCount.incrementAndGet();
        logQueue.add(buildLog("DEBUG", message, getModel()));
    }

    public static void info(String message) {
        logger.info(message);
        totalCount.increment();
        currentSecondCount.incrementAndGet();
        logQueue.add(buildLog("INFO", message, getModel()));
    }

    public static void warn(String message) {
        logger.warn(message);
        totalCount.increment();
        currentSecondCount.incrementAndGet();
        logQueue.add(buildLog("WARN", message, getModel()));
    }

    public static void error(String message) {
        logger.error(message);
        totalCount.increment();
        errorCount.increment();
        currentSecondCount.incrementAndGet();
        logQueue.add(buildLog("ERROR", message, getModel()));
    }


    private static String getModel() {
        String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        try {
            Class<?> callerClass = Class.forName(callerClassName);
            Model annotation = callerClass.getAnnotation(Model.class);
            if (annotation != null) {
                logger.trace("获取到Model注解值: {}", annotation.type());
                return annotation.type();
            }
        } catch (ClassNotFoundException e) {
            logger.error("找不到调用者类: {}", callerClassName, e);
        }
        return "UnknownModel";
    }



    private static Log buildLog(String level, String message, String model) {
        Log log = new Log();
        log.setTimestamp(System.currentTimeMillis());
        log.setLevel(level);
        log.setContext(message);
        log.setModel(model);
        log.setProjectId(qgAPIClientConfig.qgApiClient().getProjectToken());
        return log;
    }


}