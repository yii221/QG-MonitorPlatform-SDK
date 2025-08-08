package com.pmpsdk.client;


public class QGAPIClient {

    private String projectToken;
    private String environment = "test"; // 默认环境为生产环境
    private int maliciousThreshold = 3; // 默认阈值3次/秒


    public QGAPIClient() {
    }

    public QGAPIClient(String projectToken, String environment, int maliciousThreshold) {
        this.projectToken = projectToken;
        this.environment = environment;
        this.maliciousThreshold = maliciousThreshold;
    }

    /**
     * 获取
     * @return projectToken
     */
    public String getProjectToken() {
        return projectToken;
    }

    /**
     * 设置
     * @param projectToken
     */
    public void setProjectToken(String projectToken) {
        this.projectToken = projectToken;
    }

    /**
     * 获取
     * @return environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * 设置
     * @param environment
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * 获取
     * @return maliciousThreshold
     */
    public int getMaliciousThreshold() {
        return maliciousThreshold;
    }

    /**
     * 设置
     * @param maliciousThreshold
     */
    public void setMaliciousThreshold(int maliciousThreshold) {
        this.maliciousThreshold = maliciousThreshold;
    }

    public String toString() {
        return "QGAPIClient{projectToken = " + projectToken + ", environment = " + environment + ", maliciousThreshold = " + maliciousThreshold + "}";
    }
}