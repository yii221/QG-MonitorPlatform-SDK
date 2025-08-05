package com.pmpsdk.utils;

import cn.hutool.core.util.StrUtil;
import com.pmpsdk.domain.EnvironmentSnapshot;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.Version;

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

        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        // 解析浏览器信息
        Browser browser = userAgent.getBrowser();
        snapshot.setBrowserName(browser.getName());
        Version browserVersion = userAgent.getBrowserVersion();
        if (browserVersion != null) {
            snapshot.setBrowserVersion(browserVersion.getVersion());
        }

        // 解析操作系统信息
        OperatingSystem os = userAgent.getOperatingSystem();
        snapshot.setOsName(os.getName());

        // 解析设备信息
        DeviceType deviceType = os.getDeviceType();
        snapshot.setDeviceType(deviceType.getName());

        // 如果是移动设备发送的请求
        if (deviceType == DeviceType.MOBILE || deviceType == DeviceType.TABLET) {
            parseMobileDeviceInfo(userAgentString, snapshot);
        }

        return snapshot;
    }

    /**
     * 解析移动设备信息
     * @param userAgentString
     * @param snapshot
     */
    private static void parseMobileDeviceInfo(String userAgentString, EnvironmentSnapshot snapshot) {
        if (userAgentString.contains("iPhone")) {
            snapshot.setDeviceBrand("Apple");
            snapshot.setDeviceModel("iPhone");
        } else if (userAgentString.contains("iPad")) {
            snapshot.setDeviceBrand("Apple");
            snapshot.setDeviceModel("iPad");
        } else if (userAgentString.contains("Android")) {
            snapshot.setDeviceBrand("Android");
            // 从UA中提取具体型号
            if (userAgentString.contains("SM-")) {
                snapshot.setDeviceBrand("Samsung");
                snapshot.setDeviceModel(userAgentString.substring(
                        userAgentString.indexOf("SM-"),
                        userAgentString.indexOf(" ", userAgentString.indexOf("SM-"))
                ));
            }
        }
    }
}