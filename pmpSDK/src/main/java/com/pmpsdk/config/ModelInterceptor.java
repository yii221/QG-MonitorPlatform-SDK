package com.pmpsdk.config;

import com.pmpsdk.annotation.Model;
import com.pmpsdk.domain.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.util.logging.Handler;

/**
 * @Description: 模块拦截器  // 类说明
 * @ClassName: ModelInterceptor    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 17:12   // 时间
 * @Version: 1.0     // 版本
 */
@Configuration
public class ModelInterceptor implements HandlerInterceptor {


    private String type;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {



        if (handler instanceof HandlerMethod handlerMethod) {
            Model modelAnnotation = handlerMethod.getBeanType().getAnnotation(Model.class);
            if (modelAnnotation == null) {
                System.out.println("没有Model注解");
            } else {
                type = modelAnnotation.type();
                System.out.println(type);
            }/*
            type = modelAnnotation.type();
            System.out.println(type);*/

        }
        Message message = new Message();
        message.setModel(type);

        request.setAttribute("message", message);



        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
