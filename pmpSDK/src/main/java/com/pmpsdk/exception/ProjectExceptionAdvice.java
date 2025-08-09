package com.pmpsdk.exception;

import com.pmpsdk.annotation.Module;
import com.pmpsdk.aspect.SecurityCheckAspect;
import com.pmpsdk.client.QGAPIClient;
import com.pmpsdk.domain.EnvironmentSnapshot;
import com.pmpsdk.domain.ErrorMessage;
import com.pmpsdk.domain.TimedEnvironmentSnapshot;
import com.pmpsdk.utils.LogUtil;
import com.pmpsdk.utils.PostToServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;


@Order(4)
@RestControllerAdvice
public class ProjectExceptionAdvice {

    @Resource
    private QGAPIClient qgAPIClient;

    /**
     * 处理未被注解标记的异常
     *
     * @param ex
     * @throws Exception
     */
    @ExceptionHandler(SDKException.class)
    public void dealSDKException(SDKException ex) throws Exception {
        System.out.println("==>\n捕获到sdk内部异常" + ex.getClass().getName() +
                ":\n" + ex.getMessage() + "\n<==");
        errorMethod(ex);
    }


    /**
     * 处理未被注解标记的异常
     *
     * @param ex
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public void catchException(Exception ex) throws Exception {
        System.out.println("==>\n捕获到" + ex.getClass().getName() +
                "异常:\n" + ex.getMessage() + "\n<==");
        errorMethod(ex);
    }


    /**
     * 获取异常信息
     *
     * @param ex
     * @throws Exception
     */
    private void errorMethod(Exception ex) throws Exception {

        // 获取环境快照
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        TimedEnvironmentSnapshot timedSnapshot = SecurityCheckAspect.environmentSnapshot.get(ip);
        EnvironmentSnapshot environmentSnapshot = timedSnapshot != null ? timedSnapshot.getSnapshot() : null;

        // 获取异常类型
        Class<?> exceptionClass = ex.getClass();

        // 获取异常发生的类和方法
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 0) {
            String errorClass = stackTrace[0].getClassName();
            String errorMethod = stackTrace[0].getMethodName();
            int lineNumber = stackTrace[0].getLineNumber();

            // 构建异常信息对象
            ErrorMessage message = new ErrorMessage();
            message.setEnvironmentSnapshot(environmentSnapshot);
            message.setErrorType(exceptionClass.getSimpleName());

            message.setStack("出错类: " + errorClass + "\n" +
                    "异常类型: " + exceptionClass.getSimpleName() + "\n" +
                    "出错方法: " + errorMethod + "\n" +
                    "出错行号: " + lineNumber + "\n" +
                    "异常信息: " + ex.getMessage());

            message.setTimestamp(LocalDateTime.now());

            // 获取模块注解内容
            Class<?> clazz = Class.forName(errorClass);
            Module modelAnnotation = clazz.getAnnotation(Module.class);

            if (modelAnnotation != null && modelAnnotation.type() != null) {
                message.setModule(modelAnnotation.type());
            } else {
                message.setModule("unknown");
            }

            // 获取项目Token和运行环境
            message.setProjectId(qgAPIClient.getProjectToken());
            message.setEnvironment(qgAPIClient.getEnvironment());

            // 进行日志记录
            logError(message);

        }

    }

    /**
     * 错误日志记录
     *
     * @param message
     * @throws Exception
     */
    private void logError(ErrorMessage message) throws Exception {
        PostToServer.sendErrorMessage(message);
        LogUtil.error("异常上报=>" + message.getErrorType()
                , message.getModule() != null ? message.getModule() : "unknown");
    }

    //    private static final ConcurrentHashMap<String, AtomicInteger> exceptionCount = new ConcurrentHashMap<>();
//    private static final ConcurrentHashMap<String, Long> lastAlertTime = new ConcurrentHashMap<>();
//
//    /**
//     * 异常阈值配置
//     */
//    private static final int EXCEPTION_THRESHOLD = 10;
//    private static final long ALERT_INTERVAL = TimeUnit.MINUTES.toMillis(15);
//

//
//    /**
//     * 异常数量计数器，每分钟重置
//     *
//     *
//     */
//    static {
//        Executors.newSingleThreadScheduledExecutor()
//                .scheduleAtFixedRate(exceptionCount::clear, 1, 1, TimeUnit.MINUTES);
//    }


//    /**
//     * 统计异常数量
//     *
//     * @param exceptionType
//     * @param module
//     */
//    private void countException(String exceptionType, String module) {
//        String key = module + ":" + exceptionType;
//        exceptionCount.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
//    }
//
//    /**
//     * 判断是否需要发送告警
//     *
//     * @param exceptionType
//     * @param module
//     * @return
//     */
//    private boolean shouldAlert(String exceptionType, String module) {
//        String key = module + ":" + exceptionType;
//        int count = exceptionCount.getOrDefault(key, new AtomicInteger(0)).get();
//
//        // 超过阈值
//        if (count >= EXCEPTION_THRESHOLD) {
//            long lastTime = lastAlertTime.getOrDefault(key, 0L);
//            long currentTime = System.currentTimeMillis();
//
//            // 首次超过阈值或已过冷却期
//            if (lastTime == 0 || currentTime - lastTime > ALERT_INTERVAL) {
//                lastAlertTime.put(key, currentTime);
//
//                // 如果是重复告警，检查是否达到升级条件
//                if (lastTime != 0 && count >= EXCEPTION_THRESHOLD * 3) {
//                    AlertUtil.upgradeAlert(module, exceptionType, count);
//                }
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 发送告警
//     *
//     * @param exceptionType
//     * @param module
//     * @param count
//     * @param errorMessage
//     */
//    private void sendAlert(String exceptionType, String module, int count, ErrorMessage errorMessage) {
//        String alertMessage = String.format(
//                "【异常告警】模块: %s, 异常类型: %s, 当前次数: %d, 超过阈值: %d\n" +
//                        "最近错误信息: %s",
//                module, exceptionType, count, EXCEPTION_THRESHOLD, errorMessage.getStack());
//
//       // AlertUtil.sendAlert(alertMessage);
//    }
}

