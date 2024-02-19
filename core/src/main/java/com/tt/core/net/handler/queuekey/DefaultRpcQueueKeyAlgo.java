package com.tt.core.net.handler.queuekey;

import com.tt.core.message.MessageEntry;

/**
 * 默认rpc key，根据entry id进行处理
 */
public class DefaultRpcQueueKeyAlgo implements IRpcQueueKeyAlgo {
    @Override
    public int getQueueKey(MessageEntry entry, Object[] params) {
        return entry.getId();
    }
}
