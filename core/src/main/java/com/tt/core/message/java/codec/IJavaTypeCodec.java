package com.tt.core.message.java.codec;

import io.netty.buffer.ByteBuf;

public interface IJavaTypeCodec<T> {
    /**
     * 解码
     *
     * @param buf
     * @return
     */
    T decode(ByteBuf buf);

    /**
     * 编码
     *
     * @param value
     * @param buf
     */
    int encode(T value, ByteBuf buf);

    /**
     * 编码后放入数组中
     *
     * @param value
     * @return
     */
    byte[] encode(T value);
}
