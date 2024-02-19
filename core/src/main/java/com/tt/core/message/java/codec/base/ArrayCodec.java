package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.BaseReferenceCodec;
import com.tt.core.message.java.codec.IJavaTypeCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数组类型解析
 */
@SuppressWarnings("rawtypes")
public class ArrayCodec extends BaseReferenceCodec {

    private final Class<?> type;
    private volatile IJavaTypeCodec<Object> valueCodec;
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static final Map<Class<?>, Object> DEFAULT_ARRAY = new ConcurrentHashMap<>();

    public ArrayCodec(Class clz) {
        this.type = clz;
    }


    @Override
    @SuppressWarnings("ConstantConditions")
    public Object decode(ByteBuf buf) {
        int length = JavaCodecUtil.readValue(buf, int.class);
        if (length == 0) {
            return DEFAULT_ARRAY.computeIfAbsent(type, (t) -> Array.newInstance(t, 0));
        }
        Object result = Array.newInstance(type, length);
        for (int i = 0; i < length; i++) {
            Array.set(result, i, valueCodec.decode(buf));
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int encode(Object value, ByteBuf buf) {
        if (value == null) {
            value = EMPTY_ARRAY;
        }
        int valueLength = Array.getLength(value);
        int totalLength = JavaCodecUtil.writeValue(buf, valueLength);
        for (int i = 0; i < valueLength; i++) {
            totalLength += valueCodec.encode(Array.get(value, i), buf);
        }
        return totalLength;
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte[] encode(Object value) {
        if (value == null) {
            value = EMPTY_ARRAY;
        }
        int valueLength = Array.getLength(value);
        List<byte[]> byteDatum = new ArrayList<>(valueLength);
        for (int i = 0; i < valueLength; i++) {
            byte[] data = valueCodec.encode(Array.get(value, i));
            byteDatum.add(data);
        }
        return JavaCodecUtil.bytesListToByteArray(valueLength, byteDatum);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void init(Type type, Class clz, Set parents) {
        Class<?> componentType = clz.getComponentType();
        checkReferenceLoop(componentType, parents);
        valueCodec = JavaCodecUtil.getOrRegisterCodec(this.type, parents);
    }

}
