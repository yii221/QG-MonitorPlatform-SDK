package com.pmpsdk.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.HandlerInterceptor;


/**
 * @Description: 异常拦截器  // 类说明
 * @ClassName: RequestErrorConfig    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 19:45   // 时间
 * @Version: 1.0     // 版本
 */
@Configuration
public class RequestErrorConfig implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("进入异常处理拦截器");

        return true; // 返回true表示继续处理请求，返回false则中断请求
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
