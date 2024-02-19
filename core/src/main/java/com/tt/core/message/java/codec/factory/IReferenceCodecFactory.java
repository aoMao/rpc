package com.tt.core.message.java.codec.factory;

import com.tt.core.message.java.codec.BaseReferenceCodec;

import java.lang.reflect.Type;

/**
 * 引用类型
 *
 * @param <T>
 */
public interface IReferenceCodecFactory<T extends BaseReferenceCodec> {

    /**
     * 创建新的引用类型编解码实例
     *
     * @param type
     * @param clz
     * @return
     */
    T create(Type type, Class<?> clz);

    /**
     * 校验
     *
     * @param type
     * @param clz
     * @return
     */
    boolean check(Type type, Class<?> clz);
}
