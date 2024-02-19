package com.tt.core.message.java;

import com.tt.core.message.IMessageDecodeAndEncode;
import com.tt.core.message.java.codec.JavaCodecUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Type;

/**
 * java类型编解码
 */
public class JavaMessageDecodeAndEncode implements IMessageDecodeAndEncode<Object> {

    @Override
    public Object decode(ByteBuf buf, Type clz) {
        return JavaCodecUtil.readValueAndRegisterCodec(buf, clz);
    }

    @Override
    public int encode(Object o, ByteBuf buf, Type type) {
        return JavaCodecUtil.writeValueAndRegisterCodec(buf, o, type);
    }

}
