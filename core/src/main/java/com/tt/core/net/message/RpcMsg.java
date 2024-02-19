package com.tt.core.net.message;

import com.tt.core.message.MessageEntry;
import lombok.Data;

/**
 * rpc消息对象
 */
@Data
public class RpcMsg {

    private final MessageEntry entry;
    private final Object[] params;
    private int queueKey;
    private long msgSeq;

    public RpcMsg(MessageEntry entry, long msgSeq, int queueKey, Object... params) {
        this.entry = entry;
        this.params = params;
        this.msgSeq = msgSeq;
        this.queueKey = queueKey;
    }
}
