package com.tt.core.net.handler.queuekey;

import com.tt.core.message.MessageEntry;

/**
 * rpc获取queueKey
 */
public interface IRpcQueueKeyAlgo {

    /**
     * 获取key
     *
     * @param entry
     * @param params
     * @return
     */
    int getQueueKey(MessageEntry entry, Object[] params);
}
