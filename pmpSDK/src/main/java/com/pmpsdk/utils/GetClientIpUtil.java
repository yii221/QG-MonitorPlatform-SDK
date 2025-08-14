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
            LogUtil.error("GeoIP2 数据库初始化失败: " + e.getMessage(),"sdk-ip");
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
            LogUtil.warn("拦截黑名单IP: " + ip,"sdk-ip");
            return true;
        }

        // 检查本地IP白名单
        if (isLocalIp(ip)) {
            // 放行本地内网 ip
            return false;
        } else {
            // 判断IP所属国家是否放行
            return checkCountryCode(ip);
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
    private static boolean checkCountryCode(String ip) {
        try {

            if (!isGeoIpInitialized) {
                System.err.println("GeoIP 数据库未初始化，无法验证IP地域");
                return true; // 或根据业务需求返回默认值
            }

            // 获取国家编码
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse cityResponse = cityGeoReader.city(ipAddress);
            String countryCode = cityResponse.getCountry().getIsoCode();

            // 获取经纬度
            Double latitude = Optional.ofNullable(cityResponse.getLocation())
                    .map(Location::getLatitude)
                    .orElse(0.0);
            Double longitude = Optional.ofNullable(cityResponse.getLocation())
                    .map(Location::getLongitude)
                    .orElse(0.0);

            String countryName = cityResponse.getCountry().getName();
            String cityName = Optional.ofNullable(cityResponse.getCity())
                    .map(AbstractNamedRecord::getName)
                    .orElse("unknown");

            System.err.println("\n\n\n\nip：" + ip);
            System.err.println("国家：" + countryName);
            System.err.println("城市：" + cityName);
            System.err.println("latitude:" + latitude + ",longitude:" + longitude);

            if (countryCode == null) {
                LogUtil.warn("unknown ip:" + ip + ",无法识别ip来源","sdk-ip");
                // 未知国家默认拦截
                return true;
            }

            // 检查是否在允许列表中
            if (ALLOWED_COUNTRIES.contains(countryCode)) {
                LogUtil.info("country:" + cityResponse.getCountry().getName() +
                             ",city:" + cityResponse.getCity().getName() +
                             ",ip:" + ipAddress + ",latitude:" + latitude +
                             ",longitude:" + longitude + ",访问了您的项目","sdk-ip");
                return false;
            } else {
                LogUtil.error("country:" + cityResponse.getCountry().getName() +
                              ",city:" + cityResponse.getCity().getName() +
                              ",ip:" + ipAddress + ",latitude:" + latitude +
                              ",longitude:" + longitude + ",非法访问","sdk-ip");
                return true;
            }

        } catch (Exception e) {
            LogUtil.error("GeoIP库中查无此ip：" + ip,"sdk-ip");
            return true;
        }
    }
}


