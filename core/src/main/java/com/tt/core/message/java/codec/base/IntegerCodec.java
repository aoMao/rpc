package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import io.netty.buffer.ByteBuf;

public class IntegerCodec implements IJavaTypeCodec<Integer> {

    private static final int HAS_NEXT_MASK = 0x80;
    private static final byte END_MASK = 0x7F;

    @Override
    public Integer decode(ByteBuf buf) {
        int r = 0;
        int a = buf.readByte();
        int i = 0;
        while ((a & HAS_NEXT_MASK) > 0) {
            r |= ((a & END_MASK) << (i * 7));
            ++i;
            a = buf.readByte();
        }
        r |= a << (i * 7);
        return r;
    }

    @Override
    public int encode(Integer integer, ByteBuf buf) {
        int value = integer == null ? 0 : integer;
        int next = value >>> 7;
        int length = 1;
        while (next > 0) {
            buf.writeByte(HAS_NEXT_MASK | value);
            value = next;
            next = value >>> 7;
            ++length;
        }
        buf.writeByte(END_MASK & value);
        return length;
    }

    @Override
    public byte[] encode(Integer value) {
        int length = 1;
        int next = value >>> 7;
        while (next > 0) {
            next = next >>> 7;
            ++length;
        }
        byte[] bytes = new byte[length];
        next = value >>> 7;
        int i = 0;
        while (next > 0) {
            bytes[i++] = (byte) (HAS_NEXT_MASK | value);
            value = next;
            next = value >>> 7;
        }
        bytes[i] = (byte) (END_MASK & value);
        return bytes;
    }
}
