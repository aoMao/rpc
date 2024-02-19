package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.BaseReferenceCodec;
import com.tt.core.message.java.codec.IJavaTypeCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import com.tt.core.message.java.codec.exception.CodecException;
import com.tt.core.util.ReflectionUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Type;
import java.util.*;

/**
 * map类型
 */
@SuppressWarnings("rawtypes")
public class MapCodec extends BaseReferenceCodec<Map> {

    private IJavaTypeCodec keyTypeCodec;
    private IJavaTypeCodec valueTypeCodec;

    @Override
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Map decode(ByteBuf buf) {
        int length = JavaCodecUtil.readValue(buf, int.class);
        if (length == 0) {
            return Collections.emptyMap();
        }
        Map map = new HashMap();
        for (int i = 0; i < length; i++) {
            Object key = keyTypeCodec.decode(buf);
            Object value = valueTypeCodec.decode(buf);
            map.put(key, value);
        }
        return map;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public int encode(Map value, ByteBuf buf) {
        int valueLength = value == null ? 0 : value.size();
        int totalLength = JavaCodecUtil.writeValue(buf, valueLength);
        if (value != null) {
            for (Object k : value.keySet()) {
                Object v = value.get(k);
                totalLength += keyTypeCodec.encode(k, buf);
                totalLength += valueTypeCodec.encode(v, buf);
            }
        }
        return totalLength;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public byte[] encode(Map value) {
        int valueLength = value == null ? 0 : value.size();
        List<byte[]> byteDatum = new ArrayList<>(valueLength);
        if (value != null) {
            for (Object k : value.keySet()) {
                Object v = value.get(k);
                byteDatum.add(keyTypeCodec.encode(k));
                byteDatum.add(valueTypeCodec.encode(v));
            }
        }
        return JavaCodecUtil.bytesListToByteArray(valueLength, byteDatum);
    }

    @Override
    protected void init(Type type, Class<?> clz, Set<Type> parents) {
        Type[] actualTypeArguments = ReflectionUtil.getActualTypeArguments(type);
        if (actualTypeArguments == ReflectionUtil.EMPTY_TYPE_ARRAY) {
            throw new CodecException("MapCodec init error, not found actualTypeArguments by type" + type.getTypeName());
        }
        keyTypeCodec = JavaCodecUtil.getOrRegisterCodec(actualTypeArguments[0], parents);
        valueTypeCodec = JavaCodecUtil.getOrRegisterCodec(actualTypeArguments[1], parents);
    }

}
