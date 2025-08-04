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


    private String value;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进来啦2");

        if(handler instanceof HandlerMethod handlerMethod) {
            Monitor monitorAnnotation = handlerMethod.getMethodAnnotation(Monitor.class);
            if (monitorAnnotation != null) {
                Message message = (Message) request.getAttribute("message");
                message.setData(value);

                request.setAttribute("message", message);

                System.out.println(message);

                System.out.println("出去啦2");
            }
        }

        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 可以在这里处理请求完成后的逻辑
        System.out.println("请求处理完成，清理资源等操作");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("后处理方法执行，通常用于修改视图或添加模型属性2");
    }
}
