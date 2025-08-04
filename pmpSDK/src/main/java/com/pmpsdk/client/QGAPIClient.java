package com.pmpsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pmpsdk.dto.UserDTO;

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
        String result = HttpUtil.get(apiBaseUrl + "/api/getUsername/", paramMap);
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
        String result = HttpUtil.post(apiBaseUrl + "/api/getUsername/", paramMap);
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
        return hashMap;
    }

    /**
     * 获取用户名
     *
     * @param userDTO
     * @return
     */
    public String getNameByJSON(UserDTO userDTO) {
        String userStr = JSONUtil.toJsonStr(userDTO);
        HttpResponse httpResponse = HttpRequest.post(apiBaseUrl + "/api/getUsername/")
                .addHeaders(headerMap(userStr))
                .body(userStr)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
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
        // 使用hutool的SHA256签名算法
        return DigestUtil.sha256Hex(body + secretKey);
    }
}