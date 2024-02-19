package com.tt.core.message.java.codec.factory;

import com.tt.core.message.java.codec.base.EnumCodec;

import java.lang.reflect.Type;

/**
 * enum类型
 */
public class EnumCodecFactory implements IReferenceCodecFactory<EnumCodec> {
    @Override
    public EnumCodec create(Type type, Class<?> clz) {
        return new EnumCodec();
    }

    @Override
    public boolean check(Type type, Class<?> clz) {
        return type instanceof Class && clz.isEnum();
    }
}
