package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class LongCodec implements IJavaTypeCodec<Long> {

    private static final long HAS_NEXT_MASK = 0x80;
    private static final int END_MASK = 0x7F;

    private static final int END_FINAL_MASK = 0xFF;

    @Override
    public Long decode(ByteBuf buf) {
        long r = 0;
        long a = buf.readByte();
        int i = 0;
        while ((a & HAS_NEXT_MASK) > 0 && i < 8) {
            r |= ((a & END_MASK) << (i * 7));
            ++i;
            a = buf.readByte();
        }
        r |= a << (i * 7);
        return r;
    }

    @Override
    public int encode(Long aLong, ByteBuf buf) {
        long value = aLong == null ? 0 : aLong;
        long next = value >>> 7;
        int i = 8;
        while (next > 0 && i > 0) {
            buf.writeByte((int) (HAS_NEXT_MASK | value));
            value = next;
            --i;
            next = value >>> 7;
        }
        if (i == 0) {
            buf.writeByte((int) (END_FINAL_MASK & value));
            return 9;
        } else {
            buf.writeByte((int) (END_MASK & value));
            return 9 - i;
        }
    }

    @Override
    public byte[] encode(Long value) {
        int length = 1;
        long next = value >>> 7;
        int i = 8;
        while (next > 0 && i > 0) {
            --i;
            ++length;
            next = next >>> 7;
        }
        byte[] bytes = new byte[length];
        i = 8;
        next = value >>> 7;
        while (next > 0 && i > 0) {
            bytes[8 - i] = (byte) (HAS_NEXT_MASK | value);
            value = next;
            --i;
            next = value >>> 7;
        }
        if (i == 0) {
            bytes[8 - i] = (byte) (END_FINAL_MASK & value);
        } else {
            bytes[8 - i] = (byte) (END_MASK & value);
        }
        return bytes;
    }
}
