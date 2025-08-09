    package com.pmpsdk.utils;

    import com.pmpsdk.annotation.Module;
    import com.pmpsdk.aspect.SecurityCheckAspect;
    import com.pmpsdk.client.QGAPIClient;
    import com.pmpsdk.domain.EnvironmentSnapshot;
    import com.pmpsdk.domain.Log;
    import jakarta.servlet.http.HttpServletRequest;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.web.context.request.RequestContextHolder;
    import org.springframework.web.context.request.ServletRequestAttributes;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Queue;
    import java.util.concurrent.ConcurrentLinkedQueue;
    import java.util.concurrent.Executors;
    import java.util.concurrent.TimeUnit;
    import java.util.concurrent.atomic.AtomicLong;
    import java.util.concurrent.atomic.LongAdder;


    public class LogUtil {

        private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

        private static final LongAdder totalCount = new LongAdder();
        private static final LongAdder errorCount = new LongAdder();
        private static final AtomicLong currentSecondCount = new AtomicLong(0);

        private static final Queue<Log> logQueue = new ConcurrentLinkedQueue<>();


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
                    } catch (Exception e) {
                        logger.error("批量日志上报异常=>{}", e.getMessage());
                    }
                }
            }, 3, 5, TimeUnit.SECONDS);
        }


        /**
         * debug
         *
         * @param message
         */
        public static void debug(String message) {
            logger.debug(message);
            totalCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("DEBUG", message, getModule()));
        }

        /**
         * debug
         *
         * @param message
         * @param module
         */
        public static void debug(String message, String module) {
            logger.debug(message);
            totalCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("DEBUG", message, module != null ? module : getModule()));
        }

        /**
         * info
         *
         * @param message
         */
        public static void info(String message) {
            logger.info(message);
            totalCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("INFO", message, getModule()));
        }

        /**
         * info
         *
         * @param message
         * @param module
         */
        public static void info(String message, String module) {
            logger.info(message);
            totalCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("INFO", message, module != null ? module : getModule()));
        }

        /**
         * warn
         *
         * @param message
         */
        public static void warn(String message) {
            logger.warn(message);
            totalCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("WARN", message, getModule()));
        }

        /**
         * warn
         *
         * @param message
         * @param module
         */
        public static void warn(String message, String module) {
            logger.warn(message);
            totalCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("WARN", message, module != null ? module : getModule()));
        }


        /**
         * error
         *
         * @param message
         */
        public static void error(String message) {
            logger.error(message);
            totalCount.increment();
            errorCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("ERROR", message, getModule()));
        }

        /**
         * error
         *
         * @param message
         * @param module
         */
        public static void error(String message, String module) {
            logger.error(message);
            totalCount.increment();
            errorCount.increment();
            currentSecondCount.incrementAndGet();
            logQueue.add(buildLog("ERROR", message, module != null ? module : getModule()));
        }

        /**
         * 获取当前模块名
         *
         * @return
         */
        private static String getModule() {
            String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            try {
                Class<?> callerClass = Class.forName(callerClassName);
                Module annotation = callerClass.getAnnotation(Module.class);
                if (annotation != null) {
                    return annotation.type();
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("找不到调用者类: " + callerClassName, e);
            }
            return "unknown";
        }


        /**
         * 构建日志对象
         *
         * @param level
         * @param message
         * @param module
         * @return
         */
        private static Log buildLog(String level, String message, String module) {
            Log log = new Log();
            log.setTimestamp(System.currentTimeMillis());
            log.setLevel(level);
            log.setContext(message);
            log.setModule(module);

            HttpServletRequest request = null;
            try {
                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    request = attrs.getRequest();
                }
            } catch (Exception ignored) {
            }

            if (request != null) {
                String ip = GetClientIpUtil.getClientIp(request);
                EnvironmentSnapshot snapshot = SecurityCheckAspect.environmentSnapshot.get(ip);
                log.setEnvironmentSnapshot(snapshot);
            }

            QGAPIClient client = SpringContextUtil.getBean(QGAPIClient.class);
            if (client != null) {
                log.setProjectId(client.getProjectToken());
                log.setEnvironment(client.getEnvironment());
            }

            return log;
        }


    }