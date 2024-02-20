package com.tt.gate.lb.algorithm;

import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;
import com.tt.message.entity.server.ServerInfo;

import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 一致性hash算法
 */
public class ConsistentHashAlgo implements IBalanceAlgo {

    private static final int VIRTUAL_HASH_CNT = 3;
    private final Map<Integer, SortedMap<Integer, Session>> msgIdSortedMapMap = new ConcurrentHashMap<>();

    @Override
    public Session getNetSession(Session session, int msgId, int queueKey) {
        SortedMap<Integer, Session> serverNetSessionSortedMap = msgIdSortedMapMap.get(msgId);
        if (serverNetSessionSortedMap == null || serverNetSessionSortedMap.isEmpty()) {
            return null;
        }
        int hash = FNVHash1(String.valueOf(queueKey));
        var tailMap = serverNetSessionSortedMap.tailMap(hash);
        if (tailMap.isEmpty()) {
            return serverNetSessionSortedMap.get(serverNetSessionSortedMap.firstKey());
        }
        return tailMap.get(tailMap.firstKey());
    }

    @Override
    public LBType lbType() {
        return LBType.CONSISTENT_HASH;
    }

    @Override
    public void addServerSession(Session session) {
        ServerInfo serverInfo = session.getServerInfo();
        String hashKey = session.routeKey();
        serverInfo.getCanDealMsgIds().forEach((msgId) -> {
            SortedMap<Integer, Session> map = msgIdSortedMapMap.computeIfAbsent(msgId, k -> new ConcurrentSkipListMap<>());
            for (int i = 0; i < VIRTUAL_HASH_CNT; i++) {
                int hash = FNVHash1(getServerHashKey(hashKey, i));
                map.put(hash, session);
            }
        });
    }

    @Override
    public void removeServerSession(Session session) {
        String hashKey = session.routeKey();
        ServerInfo serverInfo = session.getServerInfo();
        serverInfo.getCanDealMsgIds().forEach((msgId) -> {
            SortedMap<Integer, Session> map = msgIdSortedMapMap.get(msgId);
            if (map == null) {
                return;
            }
            for (int i = 0; i < VIRTUAL_HASH_CNT; i++) {
                int hash = FNVHash1(getServerHashKey(hashKey, i));
                map.remove(hash);
            }
        });
    }

    private String getServerHashKey(String key, int index) {
        return String.format("%s-%d", key, index);
    }

    /**
     * 改进的32位FNV算法1
     *
     * @param data 字符串
     * @return int值
     */
    public static int FNVHash1(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }
}
