package com.pmpsdk.log;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pmpsdk.annotation.Model;
import com.pmpsdk.domain.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.pmpsdk.utils.PostToServer.sendMessage;

public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    // ------ 日志记录方法 ------
    public static void debug(String message) {
        logger.debug(message);
        sendMessage("DEBUG", message, getModel());
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

    public static void info(String message) {
        logger.info(message);
        sendMessage("INFO", message, getModel());
    }

    public static void warn(String message) {
        logger.warn(message);
        sendMessage("WARN", message, getModel());
    }

    public static void error(String message) {
        logger.error(message);
        sendMessage("ERROR", message, getModel());
    }

}