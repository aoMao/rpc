package com.tt.core.message.java.codec.base;

import com.tt.core.message.java.codec.IJavaTypeCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class StringCodec implements IJavaTypeCodec<String> {

    @Override
    @SuppressWarnings("ConstantConditions")
    public String decode(ByteBuf buf) {
        int length = JavaCodecUtil.readValue(buf, int.class);
        byte[] data = new byte[length];
        buf.readBytes(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public int encode(String value, ByteBuf buf) {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        int length = JavaCodecUtil.writeValue(buf, data.length);
        buf.writeBytes(data);
        return length + data.length;
    }

    @Override
    public byte[] encode(String value) {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        byte[] lengthData = JavaCodecUtil.encodeValue(data.length);
        byte[] result = new byte[data.length + lengthData.length];
        int i = 0;
        for (byte lengthDatum : lengthData) {
            result[i++] = lengthDatum;
        }
        for (byte datum : data) {
            result[i++] = datum;
        }
        return result;
    }
}
