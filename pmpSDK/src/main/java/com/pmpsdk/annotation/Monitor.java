package com.pmpsdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Description: 自定义监控注解  // 类说明
 * @ClassName: Monitor    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 16:01   // 时间
 * @Version: 1.0     // 版本
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Monitor {
}
