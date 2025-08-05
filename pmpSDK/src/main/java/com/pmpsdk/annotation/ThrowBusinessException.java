package com.pmpsdk.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ThrowBusinessException {
    int code() default 400;
    String message() default "业务异常";
}
