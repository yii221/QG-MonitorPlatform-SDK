package com.pmpsdk.config.annotation;


import java.lang.annotation.*;


/**
 * 放行黑名单注解
 * （拦截）
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireAuthPath {
}
