package com.tt.gate.lb.algorithm;

import com.tt.gate.lb.LoadBalanceManager;
import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * serverId -> server绑定
 */
public class BindAlgo implements IBalanceAlgo {

    private final Map<Integer, Session> serverIdToSessionMap = new ConcurrentHashMap<>();
    private final Map<Session, Set<Integer>> sessionToAllServerIdMap = new ConcurrentHashMap<>();

    @Override
    public Session getNetSession(Session session, int msgId, int queueKey) {
        return serverIdToSessionMap.computeIfAbsent(queueKey, k -> {
            Session bindSession = bind(session, msgId);
            sessionToAllServerIdMap.computeIfAbsent(bindSession, k1 -> new ConcurrentSkipListSet<>()).add(session.getServerId());
            return bindSession;
        });
    }

    /**
     * 第一次绑定时使用一致性hash进行处理
     *
     * @param session
     * @param msgId
     * @return
     */
    private Session bind(Session session, int msgId) {
        ConsistentHashAlgo algo = LoadBalanceManager.getInstance().getAlgo(LBType.CONSISTENT_HASH);
        return algo.getNetSession(session, msgId, session.getServerId());
    }

    @Override
    public LBType lbType() {
        return LBType.BIND_BY_ID;
    }

    @Override
    public void addServerSession(Session session) {

    }

    @Override
    public void removeServerSession(Session session) {
        Set<Integer> queueIds = sessionToAllServerIdMap.remove(session);
        if (queueIds != null) {
            for (Integer queueId : queueIds) {
                serverIdToSessionMap.remove(queueId);
            }
        }
    }
}
