package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import io.netty.buffer.ByteBuf;

public class ShortCodec implements IJavaTypeCodec<Short> {
    @Override
    public Short decode(ByteBuf buf) {
        byte[] data = new byte[2];
        buf.readBytes(data);
        short r = 0;
        r |= 0x00FF & data[0];
        r |= ((int) (data[1]) << 8);
        return r;
    }

    @Override
    public int encode(Short value, ByteBuf buf) {
        var data = encode(value);
        buf.writeBytes(data);
        return data.length;
    }

    @Override
    public byte[] encode(Short value) {
        byte[] bytes = new byte[2];
        short v = value == null ? 0 : value;
        bytes[0] = (byte) v;
        bytes[1] = (byte) (v >>> 8);
        return bytes;
    }

    public static void main(String[] args) {
        short r = -45;
        printShort(r);
    }

    private static void printShort(short r) {
        for (int i = 15; i >= 0; --i) {
            System.out.print((r >>> i) & 1);
        }
        System.out.println();
    }
}
