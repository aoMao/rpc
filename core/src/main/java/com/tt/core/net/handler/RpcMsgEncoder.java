package com.tt.core.net.handler;

import com.tt.core.message.CodecUtil;
import com.tt.core.net.message.RpcMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * rpc协议处理
 * 协议id(int)+ length(只有数据的长度int) + 消息seq(long) + 路由key(int)  + 数据
 */
public class RpcMsgEncoder extends MessageToByteEncoder<RpcMsg> {

    public static final int MIN_MSG_LENGTH = 4 + 8 + 4 + 4;

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMsg msg, ByteBuf out) throws Exception {
        var entry = msg.getEntry();
        var params = msg.getParams();
        int length = entry.getParamTypes().length;
        var writeIndex = out.writerIndex();
        out.writerIndex(writeIndex + MIN_MSG_LENGTH);
        int totalLength = 0;
        for (int i = 0; i < length; i++) {
            totalLength += CodecUtil.encode(entry.getParamTypes()[i], params[i], out);
        }
        out.markWriterIndex();
        out.writerIndex(writeIndex);
        out.writeInt(entry.getId());
        out.writeInt(totalLength);
        out.writeLong(msg.getMsgSeq());
        out.writeInt(msg.getQueueKey());
        out.resetWriterIndex();
    }
}
