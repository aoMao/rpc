package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.BaseReferenceCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import com.tt.core.message.java.codec.exception.CodecException;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("rawtypes")
public class EnumCodec extends BaseReferenceCodec<Enum> {
    /**
     * 实例对象map
     */
    private final Map<Integer, Enum> map = new HashMap<>();

    @Override
    @SuppressWarnings("ConstantConditions")
    public Enum decode(ByteBuf buf) {
        int ordinal = JavaCodecUtil.readValue(buf, int.class);
        return map.get(ordinal);
    }

    @Override
    public int encode(Enum value, ByteBuf buf) {
        if (value == null) {
            buf.writeBytes(JavaCodecUtil.INT_0_BYTES);
            return JavaCodecUtil.INT_0_BYTES.length;
        }
        return JavaCodecUtil.writeValue(buf, value.ordinal());
    }

    @Override
    public byte[] encode(Enum value) {
        // 这里可能会有错误，空的时候传默认的0
        if (value == null) {
            return JavaCodecUtil.INT_0_BYTES;
        }
        return JavaCodecUtil.encodeValue(value.ordinal());
    }

    @Override
    protected void init(Type type, Class<?> clz, Set<Type> parents) {
        var all = clz.getEnumConstants();
        try {
            var method = clz.getMethod("ordinal");
            for (Object o : all) {
                int id = (int) method.invoke(o);
                map.put(id, (Enum) o);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new CodecException(e);
        }
    }

}
