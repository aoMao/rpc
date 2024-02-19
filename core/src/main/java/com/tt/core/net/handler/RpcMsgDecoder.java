package com.tt.core.net.handler;

import com.tt.core.message.CodecUtil;
import com.tt.core.message.MessageEntry;
import com.tt.core.message.MessageManager;
import com.tt.core.net.handler.decode.AbstractMsgDecoder;
import com.tt.core.net.message.RpcMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * rpc协议处理
 * 协议id(int) + length(只有数据的长度int) + 消息id + 路由key(int) + 数据
 */
@Slf4j
public class RpcMsgDecoder extends AbstractMsgDecoder {

    public RpcMsgDecoder(MessageManager messageManager) {
        super(messageManager);
    }

    @Override
    public void callDecode(MessageEntry entry, Channel channel, ByteBuf in, List<Object> out) throws Exception {
        var msgSeq = in.readLong();
        var queueKey = in.readInt();
        var params = CodecUtil.decode(in, entry);
        RpcMsg msg = new RpcMsg(entry, msgSeq, queueKey, params);
        out.add(msg);
    }
}
