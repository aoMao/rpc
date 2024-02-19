package com.tt.core.rpc;

import com.tt.core.exception.RpcIdSameException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * rpc future管理
 */
public class RpcFutureManager {
    private static final RpcFutureManager instance = new RpcFutureManager();

    private RpcFutureManager() {

    }

    public static RpcFutureManager getInstance() {
        return instance;
    }

    private final Map<Long, CompletableFuture<Object>> futureMap = new ConcurrentHashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public CompletableFuture<Object> addFuture(long id) {
        if (futureMap.containsKey(id)) {
            throw new RpcIdSameException("rpc is is same, maybe msg is too more, id" + id);
        }
        var future = new CompletableFuture();
        futureMap.put(id, future);
        return future;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public CompletableFuture<Object> addFuture(long id, long timeout, TimeUnit unit) {
        if (futureMap.containsKey(id)) {
            throw new RpcIdSameException("rpc is is same, maybe msg is too more, id" + id);
        }
        var future = new CompletableFuture().orTimeout(timeout, unit).whenComplete((r, t) -> {
            // 报异常之后删除
            if (t != null) {
                remove(id);
            }
        });
        futureMap.put(id, future);
        return future;
    }

    public CompletableFuture<?> getFuture(long id) {
        return futureMap.get(id);
    }

    public CompletableFuture<Object> remove(long id) {
        return futureMap.remove(id);
    }
}
