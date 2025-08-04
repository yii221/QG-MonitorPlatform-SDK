package com.pmpsdk.config.annotation;


import java.lang.annotation.*;


/**
 * 放行白名单注解
 * （不拦截）
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FreeAuthPath {
}
