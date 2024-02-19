package com.tt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义在field上，用来保证field的顺序，用来实现新字段兼容
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag {
    /**
     * 根据这个值进行排序，从小到大
     *
     * @return
     */
    int value() default 0;
}
