package com.tt.core.net.handler.decode;

import com.tt.core.message.MessageEntry;
import com.tt.core.message.MessageManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AbstractMsgDecoder implements ICustomMsgDecoder {

    protected final MessageManager messageManager;

    public AbstractMsgDecoder(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public void decode(Channel channel, ByteBuf in, List<Object> out) throws Exception {
        int msgEntryId = in.readInt();
        var length = in.readInt();
        var entry = messageManager.getMessageEntryById(msgEntryId);
        if (entry == null) {
            handlerEntryNotFound(channel, msgEntryId, in, length);
            return;
        }
        callDecode(entry, channel, in, out);
    }

    public abstract void callDecode(MessageEntry entry, Channel channel, ByteBuf buf, List<Object> out) throws
            Exception;

    public void handlerEntryNotFound(Channel channel, int msgEntryId, ByteBuf buf, int length) {
        log.error("decode msg error, not found msg entry by id = {}, will close connection", msgEntryId);
        buf.skipBytes(length);
    }

    public boolean isTransferMsgDecoder() {
        return false;
    }
}
