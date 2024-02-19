package com.tt.core.net.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager extends AbstractSessionManager {

    private final Map<Integer, Session> serverNetSessionMap = new ConcurrentHashMap<>();

    private final AtomicInteger idGenerate = new AtomicInteger();

    public SessionManager() {
        addRemoveFunction((session -> {
            serverNetSessionMap.remove(session.getServerId());
        }));
    }

    @Override
    protected Session createSession(Channel channel) {
        return new Session(channel, idGenerate.incrementAndGet());
    }

    public void registerServerSession(Session session) {
        serverNetSessionMap.put(session.getServerId(), session);
        super.registerSession(session);
    }

    public Session getSessionByServerId(int serverId) {
        return serverNetSessionMap.get(serverId);
    }
}
