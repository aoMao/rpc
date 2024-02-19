package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import io.netty.buffer.ByteBuf;

public class CharCodec implements IJavaTypeCodec<Character> {
    @Override
    public Character decode(ByteBuf buf) {
        byte[] data = new byte[2];
        buf.readBytes(data);
        char c = 0;
        c |= 0x00FF & data[0];
        c |= data[1] << 8;
        return c;
    }

    @Override
    public int encode(Character value, ByteBuf buf) {
        var data = encode(value);
        buf.writeBytes(data);
        return data.length;
    }

    @Override
    public byte[] encode(Character character) {
        char c = character == null ? 0 : character;
        byte[] bytes = new byte[2];
        bytes[0] = (byte) c;
        bytes[1] = (byte) (c >>> 8);
        return bytes;
    }
}
