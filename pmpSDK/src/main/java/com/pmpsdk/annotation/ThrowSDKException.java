package com.pmpsdk.annotation;

import java.lang.annotation.*;

/**
 * 异常转换注解
 * 被此注解标记的类或方法，一旦抛出异常，均改为抛出SdkException
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ThrowSDKException {
    /**
     * 是否包含方法抛出的异常声明
     * @return 是否包含
     */
    boolean includeDeclared() default false;
}
