package com.pmpsdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 自定义注解模块  // 类说明
 * @ClassName: module    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 16:07   // 时间
 * @Version: 1.0     // 版本
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    String type() default "";
}
