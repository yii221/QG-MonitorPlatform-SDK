package com.pmpsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pmpsdk.dto.UserDTO;
import com.pmpsdk.log.LogUtils;

import java.util.HashMap;
import java.util.Map;

public class QGAPIClient {
    private String accessKey;
    private String secretKey;
    private String projectToken;
    // 错误日志上报地址
    private String sentryUrl;
    // 业务接口地址
    private String apiBaseUrl;

    /**
     * 构造客户端 API实例
     *
     * @param accessKey    访问密钥
     * @param secretKey    加密密钥
     * @param projectToken 项目令牌
     * @param sentryUrl    Sentry网页地址
     * @param apiBaseUrl   接口地址
     */
    public QGAPIClient(String accessKey, String secretKey
            , String projectToken, String sentryUrl
            , String apiBaseUrl) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.projectToken = projectToken;
        this.sentryUrl = sentryUrl;
        this.apiBaseUrl = apiBaseUrl;
        LogUtils.info("QGAPIClient initialized with accessKey: {}, sentryUrl: {}", accessKey, sentryUrl);
    }


    /**
     * 获取用户名
     *
     * @param name 用户名
     * @return 用户名
     */
    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        LogUtils.info("Sending GET request with name parameter: {}", name);
        String result = HttpUtil.get(apiBaseUrl + "/api/getUsername/", paramMap);
        LogUtils.info("Sending GET request with name parameter: {}", name);
        System.out.println(result);
        return result;
    }

    /**
     * 获取用户名
     *
     * @param name 用户名
     * @return 用户名
     */
    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        LogUtils.info("Sending POST request with name parameter: {}", name);
        String result = HttpUtil.post("http://localhost:8102/api/name/", paramMap);
        LogUtils.debug("POST request response: {}", result);
        System.out.println(result);
        return result;
    }

    /**
     * 签名安全机制
     *
     * @param body
     * @return
     */
    private Map<String, String> headerMap(String body) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(5));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        // 添加project-token到请求头
        hashMap.put("project-token", projectToken);

        LogUtils.debug("Generated headers: {}", hashMap);
        return hashMap;
    }

    /**
     * 通过JSON格式发送用户信息获取用户名
     *
     * @param userDTO 用户信息DTO对象
     * @return 服务端返回的结果
     */
    public String getNameByJSON(UserDTO userDTO) {
        String userStr = JSONUtil.toJsonStr(userDTO);
        LogUtils.info("Sending JSON POST request with user: {}", userStr);

        HttpResponse httpResponse = HttpRequest.post(sentryUrl)
                .addHeaders(headerMap(userStr))
                .body(userStr)
                .execute();

        LogUtils.info("HTTP request status: {}", httpResponse.getStatus());
        String body = httpResponse.body();
        LogUtils.debug("HTTP response body: {}", body);

        System.out.println(httpResponse.getStatus());
        System.out.println(body);
        return body;
    }


    /**
     * 生成签名
     *
     * @param body
     * @param secretKey
     * @return
     */
    public static String genSign(String body, String secretKey) {
        LogUtils.debug("Generating signature for body with length: {}", body.length());
        String sign = DigestUtil.sha256Hex(body + secretKey);
        LogUtils.debug("Signature generated successfully");
        return sign;
    }
}