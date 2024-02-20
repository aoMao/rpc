package com.tt.gate.lb.algorithm;

import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指定serverId，queueKey就是目标serverId
 */
public class ServerIdAlgo implements IBalanceAlgo {

    private final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();

    @Override
    public Session getNetSession(Session session, int msgId, int queueKey) {
        return sessionMap.get(queueKey);
    }

    @Override
    public LBType lbType() {
        return LBType.SERVER_ID;
    }

    @Override
    public void addServerSession(Session session) {
        sessionMap.put(session.getServerId(), session);
    }

    @Override
    public void removeServerSession(Session session) {
        sessionMap.remove(session.getServerId());
    }
}
