package com.tt.core.net.handler.queuekey;

import com.tt.core.message.MessageEntry;

/**
 * 根据第一个参数的hashCode获取queueKey
 */
public class FirstParamRpcQueueKeyAlgo implements IRpcQueueKeyAlgo {
    @Override
    public int getQueueKey(MessageEntry entry, Object[] params) {
        if (params.length == 0) {
            return 0;
        }
        return params[0].hashCode();
    }
}
