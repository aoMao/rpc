package com.tt.core.message.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.tt.core.message.IMessageDecodeAndEncode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

/**
 * proto协议编解码
 */
@Slf4j
public class ProtoMessageDecodeAndEncode implements IMessageDecodeAndEncode<Message> {

    @Override
    public Message decode(ByteBuf buf, Type clz) {
        Parser<? extends Message> parser = ProtoMessageUtil.getInstance().getParseByClz(clz);
        int length = buf.readInt();
        try {
            return parser.parseFrom(new ByteBufInputStream(buf, length));
        } catch (InvalidProtocolBufferException e) {
            log.error("parse message error, e:", e);
            return null;
        }
    }

    @Override
    public int encode(Message message, ByteBuf buf, Type type) {
        byte[] data = message.toByteArray();
        int length = data.length;
        buf.writeInt(length);
        buf.writeBytes(data);
        return length + 4;
    }

}
