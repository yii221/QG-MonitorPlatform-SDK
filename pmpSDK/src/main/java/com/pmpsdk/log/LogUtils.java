package com.pmpsdk.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * 日志级别
     * @param message
     */
    public static void debug(String message) {
        logger.debug(message);
    }

    public static void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
