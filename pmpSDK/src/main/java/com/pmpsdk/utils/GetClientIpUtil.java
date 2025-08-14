package com.pmpsdk.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import com.maxmind.geoip2.record.AbstractNamedRecord;
import com.maxmind.geoip2.record.Location;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Optional;
import java.util.Set;

import static com.pmpsdk.utils.IpBlacklistUtil.isBlacklisted;

public class GetClientIpUtil {

    private static boolean isGeoIpInitialized = false;

    /**
     * 合法代码（中国,含港澳台）
     **/
    private static final Set<String> ALLOWED_COUNTRIES = Set.of("CN", "HK", "MO", "TW");

    /**
     * 本地/内网IP白名单
     **/
    private static final Set<String> LOCAL_IPS = Set.of(
            "127.0.0.1", "0:0:0:0:0:0:0:1", "::1",
            "10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16"
    );

    /**
     * 该数据库用于根据IP地址来确定国家信息
     **/
    private static DatabaseReader cityGeoReader;

    static {
        try {
            // 加载数据库文件
            InputStream cityStream = new ClassPathResource("GeoLite2-City.mmdb").getInputStream();
            // 初始化 DatabaseReader
            cityGeoReader = new DatabaseReader.Builder(cityStream).build();
            isGeoIpInitialized = true;
        } catch (Exception e) {
            LogUtil.error("GeoIP2 数据库初始化失败: " + e.getMessage(), "sdk-ip");
        }
    }

    /**
     * 获取客户端 IP
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * 检查IP是否需要拦截
     *
     * @return true=拦截, false=放行
     */
    public static boolean shouldIntercept(String ip) {
        // 检查IP是否在黑名单中
        if (isBlacklisted(ip)) {
            logInterception(ip, "ip已被拉入黑名单", null, null, null, null);
            return true;
        }

        // 检查本地IP白名单
        if (isLocalIp(ip)) {
            return false;
        }

        // 判断IP所属国家是否放行
        return checkCountryCode(ip);
    }

    /**
     * 统一记录拦截日志
     */
    private static void logInterception(String ip, String reason,
                                        String country, String city,
                                        Double latitude, Double longitude) {
        String logMsg = String.format("拦截ip:%s,reason:%s,country:%s,city:%s,latitude:%s,longitude:%s",
                ip,
                reason,
                Optional.ofNullable(country).orElse("unknown"),
                Optional.ofNullable(city).orElse("unknown"),
                Optional.ofNullable(latitude).map(Object::toString).orElse("0.0"),
                Optional.ofNullable(longitude).map(Object::toString).orElse("0.0"));

        LogUtil.warn(logMsg, "sdk-ip");
    }

    private static boolean checkCountryCode(String ip) {
        try {
            // 判断GeoIP是否初始化成功
            if (!isGeoIpInitialized) {
                logInterception(ip, "GeoIP未初始化", null, null, null, null);
                return true;
            }

            // 获取IP地址、城市名称
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse cityResponse = cityGeoReader.city(ipAddress);
            String countryCode = cityResponse.getCountry().getIsoCode();
            String countryName = cityResponse.getCountry().getName();
            String cityName = Optional.ofNullable(cityResponse.getCity())
                    .map(AbstractNamedRecord::getName)
                    .orElse("unknown");

            // 获取经纬度信息
            Location location = cityResponse.getLocation();
            Double latitude = location != null ? location.getLatitude() : null;
            Double longitude = location != null ? location.getLongitude() : null;

            if (countryCode == null) {
                logInterception(ip, "ip所属国家未知", null, null, null, null);
                return true;
            }

            if (ALLOWED_COUNTRIES.contains(countryCode)) {
                return false;
            } else {
                logInterception(ip, "ip为境外访问", countryName, cityName, latitude, longitude);
                return true;
            }
        } catch (Exception e) {
            logInterception(ip, "GeoIP查询失败", null, null, null, null);
            return true;
        }
    }


    /**
     * 检查IP是否为本地IP
     *
     * @param ip
     * @return
     */
    private static boolean isLocalIp(String ip) {
        return LOCAL_IPS.stream().anyMatch(range ->
                range.contains("/") ? NetUtil.isInRange(ip, range) : range.equals(ip)
        );
    }

}


