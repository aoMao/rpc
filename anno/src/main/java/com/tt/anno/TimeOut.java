package com.tt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 超时时间
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TimeOut {

    /**
     * 超时时间
     * @return
     */
    long time() default 5;

    /**
     * 单位
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}
