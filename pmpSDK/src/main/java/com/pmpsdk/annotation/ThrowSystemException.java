package com.pmpsdk.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ThrowSystemException {
    int code() default 500;
    String message() default "系统异常";
}