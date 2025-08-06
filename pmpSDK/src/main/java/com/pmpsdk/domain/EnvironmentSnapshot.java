package com.pmpsdk.domain;

public class EnvironmentSnapshot {
    // 基础信息
    private String ip;
    private String protocol; // "http" 或 "https"
    private String httpMethod; // "GET"/"POST"等

    // 设备与浏览器
    private String browserName;
    private String browserVersion;
    private String osName;
    private String osArch; // x86/x64/ARM
    private String language; // 浏览器语言

    private boolean isAjax;


    public EnvironmentSnapshot() {
    }

    public EnvironmentSnapshot(String ip, String protocol, String httpMethod, String browserName, String browserVersion, String osName, String osArch, String language, boolean isAjax) {
        this.ip = ip;
        this.protocol = protocol;
        this.httpMethod = httpMethod;
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.osName = osName;
        this.osArch = osArch;
        this.language = language;
        this.isAjax = isAjax;
    }

    /**
     * 获取
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取
     * @return protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * 设置
     * @param protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * 获取
     * @return httpMethod
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * 设置
     * @param httpMethod
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * 获取
     * @return browserName
     */
    public String getBrowserName() {
        return browserName;
    }

    /**
     * 设置
     * @param browserName
     */
    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    /**
     * 获取
     * @return browserVersion
     */
    public String getBrowserVersion() {
        return browserVersion;
    }

    /**
     * 设置
     * @param browserVersion
     */
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    /**
     * 获取
     * @return osName
     */
    public String getOsName() {
        return osName;
    }

    /**
     * 设置
     * @param osName
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * 获取
     * @return osArch
     */
    public String getOsArch() {
        return osArch;
    }

    /**
     * 设置
     * @param osArch
     */
    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    /**
     * 获取
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 获取
     * @return isAjax
     */
    public boolean isIsAjax() {
        return isAjax;
    }

    /**
     * 设置
     * @param isAjax
     */
    public void setIsAjax(boolean isAjax) {
        this.isAjax = isAjax;
    }

    public String toString() {
        return "EnvironmentSnapshot{ip = " + ip + ", protocol = " + protocol + ", httpMethod = " + httpMethod + ", browserName = " + browserName + ", browserVersion = " + browserVersion + ", osName = " + osName + ", osArch = " + osArch + ", language = " + language + ", isAjax = " + isAjax + "}";
    }
}
