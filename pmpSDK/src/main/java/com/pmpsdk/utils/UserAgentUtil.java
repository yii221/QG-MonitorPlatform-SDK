package com.pmpsdk.utils;

import cn.hutool.core.util.StrUtil;
import com.pmpsdk.domain.EnvironmentSnapshot;
import eu.bitwalker.useragentutils.*;
public class UserAgentUtil {

    /**
     * 解析用户代理字符串
     * @param userAgentString
     * @return
     */
    public static EnvironmentSnapshot parseUserAgent(String userAgentString) {
        EnvironmentSnapshot snapshot = new EnvironmentSnapshot();

        if (StrUtil.isBlank(userAgentString)) {
            return snapshot;
        }

        try {
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

            // 解析浏览器信息
            parseBrowserInfo(userAgent, snapshot);

            // 解析操作系统信息
            parseOSInfo(userAgent, snapshot);

            // 解析CPU架构
            snapshot.setOsArch(detectCpuArchitecture(userAgentString));

            return snapshot;
        } catch (Exception e) {
            LogUtil.error("UserAgent解析失败: " + e.getMessage());
            return snapshot;
        }
    }

    /**
     * 解析浏览器信息
     */
    private static void parseBrowserInfo(UserAgent userAgent, EnvironmentSnapshot snapshot) {
        Browser browser = userAgent.getBrowser();
        if (browser != null) {
            snapshot.setBrowserName(browser.getName());
        }

        Version browserVersion = userAgent.getBrowserVersion();
        if (browserVersion != null) {
            snapshot.setBrowserVersion(formatVersion(browserVersion.getVersion()));
        }
    }

    /**
     * 解析操作系统信息
     */
    private static void parseOSInfo(UserAgent userAgent, EnvironmentSnapshot snapshot) {
        OperatingSystem os = userAgent.getOperatingSystem();
        if (os != null) {
            snapshot.setOsName(os.getName());
        }
    }

    /**
     * 检测CPU架构
     */
    private static String detectCpuArchitecture(String userAgentString) {
        if (userAgentString.contains("Win64") || userAgentString.contains("x64")) {
            return "x64";
        } else if (userAgentString.contains("ARM64") || userAgentString.contains("aarch64")) {
            return "ARM64";
        } else {
            return "x86";
        }
    }

    /**
     * 格式化版本号
     */
    private static String formatVersion(String version) {
        if (StrUtil.isBlank(version)) return "Unknown";
        String[] parts = version.split("\\.");
        return parts.length > 0 ? parts[0] : version;
    }
}