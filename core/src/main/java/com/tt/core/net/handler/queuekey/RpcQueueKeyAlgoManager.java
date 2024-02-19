package com.tt.core.net.handler.queuekey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpcQueueKey算法管理
 */
public class RpcQueueKeyAlgoManager {

    private RpcQueueKeyAlgoManager() {

    }

    private static final RpcQueueKeyAlgoManager instance = new RpcQueueKeyAlgoManager();

    public static RpcQueueKeyAlgoManager getInstance() {
        return instance;
    }

    private Map<Class<? extends IRpcQueueKeyAlgo>, IRpcQueueKeyAlgo> map = new ConcurrentHashMap<>();

    public IRpcQueueKeyAlgo getAlgo(Class<? extends IRpcQueueKeyAlgo> clz) {
        var algo = map.get(clz);
        if (algo != null) {
            return algo;
        }

        try {
            var constructor = clz.getConstructor();
            var iRpcQueueKeyAlgo = constructor.newInstance();
            return map.computeIfAbsent(clz, k -> iRpcQueueKeyAlgo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
