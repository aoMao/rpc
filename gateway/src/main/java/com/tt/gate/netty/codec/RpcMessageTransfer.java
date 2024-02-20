package com.tt.gate.netty.codec;

import com.tt.core.message.MessageManager;
import com.tt.core.net.handler.RpcMsgDecoder;
import com.tt.core.net.session.SessionManager;
import com.tt.gate.lb.LoadBalanceManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * rpc消息转发
 */
@Slf4j
public class RpcMessageTransfer extends RpcMsgDecoder {

    private final SessionManager sessionManager;

    public RpcMessageTransfer(MessageManager messageManager, SessionManager sessionManager) {
        super(messageManager);
        this.sessionManager = sessionManager;
    }

    @Override
    public void handlerEntryNotFound(Channel channel, int msgEntryId, ByteBuf in, int length) {
        long msgSeq = in.readLong();
        int queueKey = in.readInt();
        // 当前连接
        var curSession = sessionManager.getSession(channel);
        // 目标连接
        var toSession = LoadBalanceManager.getInstance().getNetSession(curSession, msgEntryId, queueKey);
        if (toSession == null) {
            in.skipBytes(length);
            log.warn("rpc transfer msg not found toSession, msgEntryId: {}, msgSeq: {}, queueKey: {}, curSession: {}", msgEntryId, msgSeq, queueKey, curSession);
            return;
        }
        // 直接发送
        var compositeBuf = new CompositeByteBuf(in.alloc(), in.isDirect(), in.writableBytes() - 8);
        var headBuf = in.alloc().buffer();
        headBuf.writeInt(msgEntryId);
        headBuf.writeInt(length);
        // 以便返回结果处理
        headBuf.writeLong(msgSeq);
        headBuf.writeInt(curSession.getServerId());
        compositeBuf.addComponent(headBuf);
        compositeBuf.addComponent(in.copy());
        toSession.writeAndFlush(compositeBuf);
        in.skipBytes(length);
    }
}
