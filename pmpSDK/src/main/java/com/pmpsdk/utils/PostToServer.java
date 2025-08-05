package com.pmpsdk.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.pmpsdk.domain.ErrorMessage;
import com.pmpsdk.domain.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.pmpsdk.utils.PostUrl.ERROR;
import static com.pmpsdk.utils.PostUrl.LOG;

public final class PostToServer {
    private static final Logger logger = LoggerFactory.getLogger(PostToServer.class);

    /**
     * 发送日志
     */
    public static String postLogJSON(String json) {
        try {
            return HttpUtil.post(LOG.getUrl(), json);
        } catch (Exception e) {
            logger.error("===Q=G==>远程服务器繁忙，请稍后再尝试发送日志...", e);
            return StrUtil.EMPTY;
        }
    }

    /**
     * 发送错误
     */
    public static String postErrorJSON(String json) {
        try {
            return HttpUtil.post(ERROR.getUrl(), json);
        } catch (Exception e) {
            logger.error("===Q=G==>远程服务器繁忙，请稍后再尝试发送日志...", e);
            return StrUtil.EMPTY;
        }
    }

    /**
     * 发送任意 json
     */
    public static String postJSON(String url, String json) {
        try {
            return HttpUtil.post(url, json);
        } catch (Exception e) {
            logger.error("===Q=G==>远程服务器繁忙，请稍后再尝试发送数据...", e);
            return StrUtil.EMPTY;
        }
    }

    public static void sendMessage(ErrorMessage message) {
        try {
            JSON json = cn.hutool.json.JSONUtil.parse(message);
            logger.debug("准备发送消息: {}", json.toString());

            String response = postErrorJSON(json.toString());
            logger.debug("错误日志上报响应: {}", response);
        } catch (Exception e) {
            logger.error("消息发送失败", e);
        }
    }


    public static void sendToServer(String level, String message, String model) {
        try {
            Log log = new Log();
            log.setTimestamp(System.currentTimeMillis());
            log.setLevel(level);
            log.setContext(message);
            log.setModel(model);

            String json = JSONUtil.toJsonStr(log);
            logger.debug("准备上报日志: {}", json);

            String response = postLogJSON(json);
            logger.debug("日志上报响应: {}", response);
        } catch (Exception e) {
            logger.error("日志上报异常", e);
        }
    }
}