package com.tt.core.message;

import com.google.protobuf.Message;
import com.tt.core.message.java.JavaMessageDecodeAndEncode;
import com.tt.core.message.proto.ProtoMessageDecodeAndEncode;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Type;

public class CodecUtil {
    private static final ProtoMessageDecodeAndEncode protoMessageCodec = new ProtoMessageDecodeAndEncode();
    private static final JavaMessageDecodeAndEncode defaultCodec = new JavaMessageDecodeAndEncode();

    /**
     * 解码，这里仅有data的解码，其他数据在之前已经处理
     *
     * @param buf
     * @param entry
     * @return
     */
    public static Object[] decode(ByteBuf buf, MessageEntry entry) {
        int length = entry.getParamTypes().length;
        Object[] params = new Object[length];
        for (int i = 0; i < length; i++) {
            params[i] = decode(buf, entry.getParamTypes()[i]);
        }
        return params;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static int encode(Type type, Object value, ByteBuf buf) {
        IMessageDecodeAndEncode messageDecodeAndEncode = getMessageCodecByMsgClz(type);
        return messageDecodeAndEncode.encode(value, buf, type);
    }

    @SuppressWarnings({"rawtypes"})
    public static Object decode(ByteBuf buf, Type type) {
        IMessageDecodeAndEncode messageDecodeAndEncode = getMessageCodecByMsgClz(type);
        return messageDecodeAndEncode.decode(buf, type);
    }

    @SuppressWarnings({"rawtypes"})
    private static IMessageDecodeAndEncode getMessageCodecByMsgClz(Type type) {
        if (type instanceof Class clz) {
            if (Message.class.isAssignableFrom(clz)) {
                return protoMessageCodec;
            }
        }
        return defaultCodec;
    }
}
