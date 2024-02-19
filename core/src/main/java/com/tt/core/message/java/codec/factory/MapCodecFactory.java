package com.tt.core.message.java.codec.factory;

import com.tt.core.message.java.codec.base.MapCodec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * map类型
 */
public class MapCodecFactory implements IReferenceCodecFactory<MapCodec> {
    @Override
    public MapCodec create(Type type, Class<?> clz) {
        return new MapCodec();
    }

    @Override
    public boolean check(Type type, Class<?> clz) {
        if (type instanceof ParameterizedType) {
            return Map.class.isAssignableFrom(clz);
        }
        return false;
    }
}
