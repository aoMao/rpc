package com.tt.core.message.java.codec;

import com.tt.core.message.java.codec.exception.CodecException;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;

/**
 * 引用类型编解码基类
 *
 * @param <T>
 */
public abstract class BaseReferenceCodec<T> implements IJavaTypeCodec<T> {


    /**
     * 初始化逻辑
     *
     * @param type
     * @param parents
     */
    @SuppressWarnings({"rawtypes"})
    protected abstract void init(Type type, Class<?> clz, Set<Type> parents);

    /**
     * 检查是否有循环引用
     *
     * @param type
     * @param parents
     */
    @SuppressWarnings({"rawtypes"})
    public void checkReferenceLoop(Type type, Set<Type> parents) {
        for (Type parent : parents) {
            if (Objects.equals(parent, type)) {
                throw new CodecException(String.format("loop reference in %s", parent.getTypeName()));
            }
        }
    }
}
