package com.pmpsdk.log;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pmpsdk.annotation.Model;
import com.pmpsdk.domain.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    // ------ 日志记录方法（同步本地日志和HTTP上报） ------
    public static void debug(String message) {
        logger.debug(message);
        String model = getModel();
        sendToServer("DEBUG", message, model);
    }

    private static String  getModel() {
        // 获取调用者类名
        String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        try {
            Class<?> callerClass = Class.forName(callerClassName);
            // 假设你要获取 @YourAnnotation 注解
            Model annotation = callerClass.getAnnotation(Model.class);
            if (annotation != null) {
                // 这里可以处理注解内容
                System.out.println("注解值: " + annotation.type());
            return annotation.type();
            }
        } catch (ClassNotFoundException e) {
            logger.error("找不到调用者类", e);
        }
        return "UnknownModel";
    }

    public static void info(String message) {
        logger.info(message);

        sendToServer("INFO", message, getModel());
    }

    public static void warn(String message) {
        logger.warn(message);
        sendToServer("WARN", message, getModel());
    }

    public static void error(String message) {
        logger.error(message);
        sendToServer("ERROR", message, getModel());
    }


    // ------ 私有方法：HTTP上报逻辑 ------
    private static void sendToServer(String level, String message, String model) {

        try {
            Log log = new Log();
            log.setTimestamp(System.currentTimeMillis());
            log.setLevel(level);
            log.setContext(message);
            log.setModel(model);


            String response = HttpUtil.post("http://192.168.1.233:8080/messages/log", JSONUtil.toJsonStr(log));

            System.out.println("日志上报响应: " + response);

        } catch (Exception e) {
            logger.error("日志上报异常", e);
        }
    }



}