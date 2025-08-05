package com.pmpsdk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: MVC拦截器  // 类说明
 * @ClassName: MvcConfig    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 17:15   // 时间
 * @Version: 1.0     // 版本
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new ModuleInterceptor()).excludePathPatterns("/errors").order(1);
        //registry.addInterceptor(new MonitorInterceptor()).excludePathPatterns("/errors").order(0);
        registry.addInterceptor(new IpLoggingInterceptor()).excludePathPatterns("/error").order(0);

    }
}
