package com.tt.core.message.java.codec.factory;

import com.tt.core.message.java.codec.collection.SetCodec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * set类型
 */
public class SetCodecFactory implements IReferenceCodecFactory<SetCodec> {
    @Override
    public SetCodec create(Type type, Class<?> clz) {
        return new SetCodec();
    }

    @Override
    public boolean check(Type type, Class<?> clz) {
        if (type instanceof ParameterizedType) {
            return Set.class.isAssignableFrom(clz);
        }
        return false;
    }
}
