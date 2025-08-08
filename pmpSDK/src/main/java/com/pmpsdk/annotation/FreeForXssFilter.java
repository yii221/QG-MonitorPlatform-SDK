package com.pmpsdk.annotation;

import java.lang.annotation.*;


/**
 * 忽略XSS过滤
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FreeForXssFilter {
}
