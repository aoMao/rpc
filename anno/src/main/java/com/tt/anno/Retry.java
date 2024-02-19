package com.tt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试次数
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    int DEFAULT_RETRY_TIMES = 2;
    /**
     * 重试多少次
     *
     * @return
     */
    int value() default DEFAULT_RETRY_TIMES;
}
