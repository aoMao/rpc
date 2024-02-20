package com.tt.core.gatelb;

import com.tt.core.gatelb.algo.ConsistentHashAlgo;
import com.tt.core.gatelb.algo.IGateLbAlgo;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

import java.util.HashMap;
import java.util.Map;

/**
 * 面向gate的负载均衡，客户端向gate发送消息时会调用这个
 */
public class GateLbManager {

    private static final GateLbManager instance = new GateLbManager();
    private GateLbManager() {
        // 这里向gate发送暂时只支持一致性hash
        map.put(LBType.CONSISTENT_HASH, defaultAlgo);
    }

    public static GateLbManager getInstance() {
        return instance;
    }

    private final IGateLbAlgo defaultAlgo = new ConsistentHashAlgo();

    private final Map<LBType, IGateLbAlgo> map = new HashMap<>();

    /**
     * 根据消息获取session
     *
     * @param msg
     * @return
     */
    public Session getSession(RpcMsg msg) {
        return defaultAlgo.getSession(msg);
    }

    public void addServerSession(Session session) {
        for (IGateLbAlgo algo : map.values()) {
            algo.addServerSession(session);
        }
    }

    public void removeServerSession(Session session) {
        for (IGateLbAlgo algo : map.values()) {
            algo.removeServerSession(session);
        }
    }
}
