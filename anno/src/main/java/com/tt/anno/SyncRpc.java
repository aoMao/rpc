package com.tt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 是否是同步标记，
 * 标记在接口上，代表接口的所有方法均为同步，
 * 如果方法上使用了SyncRpc或者ASyncRpc，以方法上为准
 * 标记在方法上，代表此方法为同步获取
 * 不标记默认为同步获取
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SyncRpc {
}
