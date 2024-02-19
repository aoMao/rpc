package com.tt.message.anno;

import com.tt.message.constant.LBType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息标志，在接口上使用，标志当前接口的方法都是消息（静态方法、默认方法、private方法除外）
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadBalance {
	/**
	 * 负载策略
	 *
	 * @return
	 */
	LBType lbType() default LBType.CONSISTENT_HASH;
}
