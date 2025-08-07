package com.pmpsdk.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.pmpsdk.domain.ErrorMessage;
import com.pmpsdk.domain.Log;
import com.pmpsdk.domain.PerformanceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.SecretKey;
import java.util.List;
import java.util.Map;

import static com.pmpsdk.utils.PostUrl.*;


public final class PostToServer {

    private static final Logger postLogger = LoggerFactory.getLogger(PostToServer.class);

    static SecretKey aseKey;
    static String aesKeyStr;

    static {
        try {
            aseKey = CryptoUtil.generateAESKey(256);
            aesKeyStr = CryptoUtil.rsaEncryptAESKey(aseKey, CryptoUtil.getPublicKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 发送日志
     */
    private static String postLogJSON(String data) throws Exception {
//        try {
//            String json = getJson(data);
//            return HttpUtil.post(LOG.getUrl(), json);
//        } catch (Exception e) {
//            logger.error("\n===Q=G==>远程服务器繁忙，请稍后再尝试发送日志...", e);
//            return StrUtil.EMPTY;
//        }
        return HttpUtil.post(LOG.getUrl(), getJson(data));
    }


    /**
     * 发送错误
     */
    private static String postErrorJSON(String data) throws Exception {
//        try {
//            String json = getJson(data);
//            return HttpUtil.post(ERROR.getUrl(), json);
//        } catch (Exception e) {
//            logger.error("===Q=G==>远程服务器繁忙，请稍后再尝试发送日志...", e);
//            return StrUtil.EMPTY;
//        }
        return HttpUtil.post(ERROR.getUrl(), getJson(data));
    }

    /**
     * 发送性能
     */
    private static String postPerformanceJSON(String data) {
//        try {
//            String json = getJson(data);
//            return HttpUtil.post(LOG.getUrl(), json);
//        } catch (Exception e) {
//            logger.error("===Q=G==>远程服务器繁忙，请稍后再尝试发送性能日志...", e);
//            return StrUtil.EMPTY;
//        }
        return HttpUtil.post(PERFORMANCE.getUrl(), data);
    }


    /**
     * 发送任意 json
     */
    private static String postJSON(String url, String data) throws Exception {
//        try {
//            String json = getJson(data);
//            return HttpUtil.post(url, json);
//        } catch (Exception e) {
//            logger.error("===Q=G==>远程服务器繁忙，请稍后再尝试发送数据...", e);
//            return StrUtil.EMPTY;
//        }
        return HttpUtil.post(url, getJson(data));
    }

    public static void sendErrorMessage(ErrorMessage message) throws Exception {
//        try {
//            JSON json = cn.hutool.json.JSONUtil.parse(message);
//            logger.debug("准备发送消息: {}", json.toString());
//            String response = postErrorJSON(json.toString());
//            logger.debug("错误日志上报响应: {}", response);
//        } catch (Exception e) {
//            logger.error("消息发送失败", e);
//        }
        JSON json = cn.hutool.json.JSONUtil.parse(message);
        postErrorJSON(json.toString());
    }


    public static void sendLogMessage(List<Log> logs) throws Exception {
//        try {
//
//            String json = JSONUtil.toJsonStr(logs);
//            logger.debug("准备上报日志: {}", json);
//
//            String response = postLogJSON(json);
//            logger.debug("日志上报响应: {}", response);
//        } catch (Exception e) {
//            logger.error("日志上报异常", e);
//        }
        String json = JSONUtil.toJsonStr(logs);
        postLogJSON(json);
    }

    public static void sendPerformanceLogMessage(List<PerformanceLog> logs) {
//        try {
//            String json = JSONUtil.toJsonStr(logs);
//            logger.debug("准备上报性能日志: {}", json);
//
//            String response = postPerformanceJSON(json);
//            logger.debug("性能日志上报响应: {}", response);
//        } catch (Exception e) {
//            logger.error("性能日志上报异常", e);
//        }
        String json = JSONUtil.toJsonStr(logs);
        postPerformanceJSON(json);
    }

    /**
     * 发送通用JSON到任意URL
     */
    public static String sendJson(String url, String jsonData) throws Exception {
//        logger.debug("准备发送JSON到: {}", url);
        //        logger.debug("服务器响应: {}", response);
        return postJSON(url, jsonData);
    }

    /**
     * 发送方法统计情况
     *
     * @param statistics
     * @return
     * @throws Exception
     */
    public static void sendMethodInvocationStats(Map<String, Integer> statistics) throws Exception {
        postJSON(PostUrl.METHOD_USE_COUNT.getUrl(), JSONUtil.toJsonStr(statistics));
    }

    /**
     * @param data
     * @return java.lang.String
     * @Author lrt
     * @Description //TODO 将数据加密成json
     * @Date 10:31 2025/8/6
     * @Param
     **/
    private static String getJson(String data) throws Exception {
        String s = CryptoUtil.aesEncrypt(data, aseKey);
        String json = JSONUtil.createObj()
                .set("aesKey", aesKeyStr)
                .set("data", s)
                .toString();
        return json;
    }

}