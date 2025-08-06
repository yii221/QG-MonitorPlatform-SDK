package com.pmpsdk.client;

import cn.hutool.crypto.digest.DigestUtil;
import com.pmpsdk.annotation.ThrowSDKException;

@ThrowSDKException
public class QGAPIClient {

    private String projectToken;
    private String environment = "test"; // 默认环境为生产环境

    /**
     * 构造客户端 API实例
     *
     * @param projectToken 项目令牌
     * @param environment  环境
     */
    public QGAPIClient(String projectToken, String environment) {
        this.projectToken = projectToken;
        this.environment = environment;
        System.err.println("QGAPIClient:" + projectToken + ":" + environment);
    }

    public QGAPIClient() {
    }


    /**
     * 生成签名
     *
     * @param body
     * @param secretKey
     * @return
     */
    public static String genSign(String body, String secretKey) {

        String sign = DigestUtil.sha256Hex(body + secretKey);

        return sign;
    }



    /**
     * 获取
     *
     * @return projectToken
     */
    public String getProjectToken() {
        return projectToken;
    }

    /**
     * 设置
     *
     * @param projectToken
     */
    public void setProjectToken(String projectToken) {
        this.projectToken = projectToken;
    }

    /**
     * 获取
     *
     * @return environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * 设置
     *
     * @param environment
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

}