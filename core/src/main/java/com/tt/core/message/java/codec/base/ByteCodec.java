package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import io.netty.buffer.ByteBuf;

public class ByteCodec implements IJavaTypeCodec<Byte> {
    @Override
    public Byte decode(ByteBuf buf) {
        return buf.readByte();
    }

    @Override
    public int encode(Byte value, ByteBuf buf) {
        buf.writeByte(value == null ? 0 : value);
        return 1;
    }

    @Override
    public byte[] encode(Byte value) {
        byte[] bytes = new byte[1];
        bytes[0] = value == null ? 0 : value;
        return bytes;
    }
}
