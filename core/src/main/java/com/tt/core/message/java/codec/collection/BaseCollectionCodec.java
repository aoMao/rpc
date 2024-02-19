package com.tt.core.message.java.codec.collection;

import com.tt.core.message.java.codec.BaseReferenceCodec;
import com.tt.core.message.java.codec.IJavaTypeCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import com.tt.core.message.java.codec.exception.CodecException;
import com.tt.core.util.ReflectionUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 集合类型
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public abstract class BaseCollectionCodec<T extends Collection> extends BaseReferenceCodec<T> {
    @SuppressWarnings("rawtypes")
    private volatile IJavaTypeCodec valueCodec;

    @Override
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public T decode(ByteBuf buf) {
        int length = JavaCodecUtil.readValue(buf, int.class);
        if (length == 0) {
            return getEmptyInstance();
        }
        T list = createNewInstance(length);
        for (int i = 0; i < length; i++) {
            list.add(valueCodec.decode(buf));
        }
        return list;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public int encode(T value, ByteBuf buf) {
        int valueLength = value == null ? 0 : value.size();
        int totalLength = JavaCodecUtil.writeValue(buf, valueLength);
        if (value != null) {
            for (Object o : value) {
                totalLength += valueCodec.encode(o, buf);
            }
        }
        return totalLength;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public byte[] encode(T value) {
        int valueLength = value == null ? 0 : value.size();
        List<byte[]> byteDatum = new ArrayList<>(valueLength);
        if (value != null) {
            for (Object o : value) {
                byteDatum.add(valueCodec.encode(o));
            }
        }
        return JavaCodecUtil.bytesListToByteArray(valueLength, byteDatum);
    }

    @Override
    protected void init(Type type, Class<?> clz, Set<Type> parents) {
        Type[] actualTypeArguments = ReflectionUtil.getActualTypeArguments(type);
        if (actualTypeArguments == ReflectionUtil.EMPTY_TYPE_ARRAY) {
            throw new CodecException("collection codec init error, not found actualTypeArguments by type" + type.getTypeName());
        }
        Type valueType = actualTypeArguments[0];
        checkReferenceLoop(valueType, parents);
        valueCodec = JavaCodecUtil.getOrRegisterCodec(valueType, parents);
    }

    protected abstract T createNewInstance(int length);

    protected abstract T getEmptyInstance();
}
