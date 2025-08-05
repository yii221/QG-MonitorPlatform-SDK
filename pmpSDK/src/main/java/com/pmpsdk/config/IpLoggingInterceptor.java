package com.pmpsdk.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.pmpsdk.utils.GetClientIpUtil.getClientIp;


@Component
public class IpLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIp(request);
        System.out.println("====>Client IP: " + clientIp);
        request.setAttribute("clientIp", clientIp);
        return true;
    }


}