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

import static com.pmpsdk.utils.IpBlacklistUtil.isBlacklisted;

public class GetClientIpUtil {

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
    private static DatabaseReader geoIpReader;

    static {
        try (InputStream dbStream = new ClassPathResource("GeoLite2-Country.mmdb").getInputStream()) {
            geoIpReader = new DatabaseReader.Builder(dbStream).build();
        } catch (Exception ignored) {
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
        // TODO：检查IP是否在黑名单中
        if (isBlacklisted(ip)) {
            LogUtil.warn("拦截黑名单IP: " + ip);
            return true;
        }

        // TODO：检查本地IP白名单
        if (isLocalIp(ip)) {
            // TODO：放行本地内网 ip
            System.err.println("\n\n放行本地ip: " + ip + "\n\n");
            return false;
        } else {
            // TODO：获取IP所属国家
            String countryCode = getCountryCode(ip);
            System.err.println("国家代码: " + countryCode);

            if (countryCode == null) {
                LogUtil.warn("不是中国IP: " + ip);
                // TODO：未知国家默认拦截
                return true;
            }
            // TODO：检查是否在允许列表中
            return !ALLOWED_COUNTRIES.contains(countryCode);
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

    /**
     * 获取该 ip的来源国家
     *
     * @param ip
     * @return
     */
    private static String getCountryCode(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = geoIpReader.country(ipAddress);
            return response.getCountry().getIsoCode();
        } catch (Exception e) {
            LogUtil.error("GeoIP找不出是哪国ip：" + ip);
            return null;
        }
    }
}
