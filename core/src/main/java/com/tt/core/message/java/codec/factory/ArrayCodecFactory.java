package com.tt.core.message.java.codec.factory;

import com.tt.core.message.java.codec.base.ArrayCodec;

import java.lang.reflect.Type;

/**
 * 数组类型创建
 */
public class ArrayCodecFactory implements IReferenceCodecFactory<ArrayCodec> {
    @Override
    public ArrayCodec create(Type type, Class<?> clz) {
        return new ArrayCodec(clz.getComponentType());
    }

    @Override
    public boolean check(Type type, Class<?> clz) {
        return clz.isArray();
    }
}
