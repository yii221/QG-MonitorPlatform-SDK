package com.pmpsdk.domain;

import lombok.Data;

@Data
public class EnvironmentSnapshot {
    // 操作系统信息
    private String osName;
    private String osArch;

    // 浏览器信息
    private String browserName;
    private String browserVersion;

    // 设备信息
    private String deviceType;

    private String deviceBrand;
    private String deviceModel;

    // 网络信息
    private String networkType;

    // 应用信息
    private String appVersion;





}
