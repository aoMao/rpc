package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import io.netty.buffer.ByteBuf;

public class DoubleCodec implements IJavaTypeCodec<Double> {
    @Override
    public Double decode(ByteBuf buf) {
        @SuppressWarnings("all")
        long l = JavaCodecUtil.readValue(buf, long.class);
        return Double.longBitsToDouble(l);
    }

    @Override
    public int encode(Double value, ByteBuf buf) {
        long l = value == null ? 0 : Double.doubleToRawLongBits(value);
        return JavaCodecUtil.writeValue(buf, l);
    }

    @Override
    public byte[] encode(Double value) {
        long l = value == null ? 0 : Double.doubleToRawLongBits(value);
        return JavaCodecUtil.encodeValue(l);
    }
}
