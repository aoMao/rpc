package com.tt.core.message.java.codec.factory;

import com.tt.core.message.java.codec.collection.ListCodec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * list类型
 */
public class ListCodecFactory implements IReferenceCodecFactory<ListCodec> {
    @Override
    public ListCodec create(Type type, Class<?> clz) {
        return new ListCodec();
    }

    @Override
    public boolean check(Type type, Class<?> clz) {
        if (type instanceof ParameterizedType) {
            return List.class.isAssignableFrom(clz);
        }
        return false;
    }
}
