package com.pmpsdk.config;

import com.pmpsdk.annotation.Model;
import com.pmpsdk.annotation.Monitor;
import com.pmpsdk.domain.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 监控拦截器  // 类说明
 * @ClassName: MonitorInterceptor    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 17:41   // 时间
 * @Version: 1.0     // 版本
 */
@Configuration
public class MonitorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod handlerMethod) {
            Monitor monitorAnnotation = handlerMethod.getMethodAnnotation(Monitor.class);
            if (monitorAnnotation != null) {
                Message message = (Message) request.getAttribute("message");

                request.setAttribute("message", message);

                System.out.println(message);

            }
        }

        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
