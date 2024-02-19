package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import io.netty.buffer.ByteBuf;

public class BooleanCodec implements IJavaTypeCodec<Boolean> {

    @Override
    public Boolean decode(ByteBuf buf) {
        return buf.readByte() == 1;
    }

    @Override
    public int encode(Boolean value, ByteBuf buf) {
        buf.writeByte(value != null && value ? 1 : 0);
        return 1;
    }

    @Override
    public byte[] encode(Boolean value) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (value != null && value ? 1 : 0);
        return bytes;
    }
}
