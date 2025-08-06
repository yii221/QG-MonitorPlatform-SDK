package com.pmpsdk.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Set;

public class GetClientIpUtil {

    /** 合法代码（中国,含港澳台） **/
    private static final Set<String> ALLOWED_COUNTRIES = Set.of("CN", "HK", "MO", "TW");


    /** 本地/内网IP白名单 **/
    private static final Set<String> LOCAL_IPS = Set.of(
            "127.0.0.1", "0:0:0:0:0:0:0:1", "::1",
            "10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16"
    );

    /** 该数据库用于根据IP地址来确定国家信息 **/
    private static DatabaseReader geoIpReader;

    static {
        try (InputStream dbStream = new ClassPathResource("GeoLite2-Country.mmdb").getInputStream()) {
            geoIpReader = new DatabaseReader.Builder(dbStream).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load GeoIP database", e);
        }
    }


    /**
     * 获取客户端 IP
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
     * @return true=拦截, false=放行
     */
    public static boolean shouldBlock(String ip) {
        // 1. 检查本地IP白名单
        if (isLocalIp(ip)) {
            return false;
        }

        // 2. 获取IP所属国家
        String countryCode = getCountryCode(ip);
        if (countryCode == null) {
            LogUtil.warn("Unknown country for IP: {}", ip);
            return true; // 未知国家默认拦截
        }

        // 3. 检查是否在允许列表中
        return !ALLOWED_COUNTRIES.contains(countryCode);
    }

    private static boolean isLocalIp(String ip) {
        return LOCAL_IPS.stream().anyMatch(range ->
                range.contains("/") ? NetUtil.isInRange(ip, range) : range.equals(ip)
        );
    }

    private static String getCountryCode(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = geoIpReader.country(ipAddress);
            return response.getCountry().getIsoCode();
        } catch (Exception e) {
            LogUtil.error("GeoIP lookup failed for IP: {}", ip);
            return null;
        }
    }
}
