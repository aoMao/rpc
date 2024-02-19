package com.tt.message.rpc;

import java.util.concurrent.CompletableFuture;

/**
 * 消息接口，标记
 */
public interface IRpc {

    default <T> CompletableFuture<T> returnAsyncObj(T result) {
        return CompletableFuture.completedFuture(result);
    }
}
