package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import io.netty.buffer.ByteBuf;

public class FloatCodec implements IJavaTypeCodec<Float> {
    @Override
    public Float decode(ByteBuf buf) {
        @SuppressWarnings("all")
        int f = JavaCodecUtil.readValue(buf, int.class);
        return Float.intBitsToFloat(f);
    }

    @Override
    public int encode(Float value, ByteBuf buf) {
        int f = value == null ? 0 : Float.floatToIntBits(value);
        return JavaCodecUtil.writeValue(buf, f);
    }

    @Override
    public byte[] encode(Float value) {
        int f = value == null ? 0 : Float.floatToIntBits(value);
        return JavaCodecUtil.encodeValue(f);
    }
}
