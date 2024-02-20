package com.tt.core.gatelb.algo;

import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 一致性hash算法
 */
public class ConsistentHashAlgo implements IGateLbAlgo {

    private static final int VIRTUAL_HASH_CNT = 3;
    private final SortedMap<Integer, Session> sortedMap = new ConcurrentSkipListMap<>();

    @Override
    public LBType lbType() {
        return LBType.CONSISTENT_HASH;
    }

    @Override
    public void addServerSession(Session session) {
        String hashKey = session.routeKey();
        for (int i = 0; i < VIRTUAL_HASH_CNT; i++) {
            int hash = FNVHash1(getServerHashKey(hashKey, i));
            sortedMap.put(hash, session);
        }
    }

    @Override
    public void removeServerSession(Session session) {
        String hashKey = session.routeKey();
        for (int i = 0; i < VIRTUAL_HASH_CNT; i++) {
            int hash = FNVHash1(getServerHashKey(hashKey, i));
            sortedMap.remove(hash);
        }
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

    @Override
    public Session getSession(RpcMsg msg) {
        if (sortedMap.isEmpty()) {
            return null;
        }

        int hash = FNVHash1(String.valueOf(msg.getQueueKey()));
        var tailMap = sortedMap.tailMap(hash);
        if (tailMap.isEmpty()) {
            return sortedMap.get(sortedMap.firstKey());
        }
        return tailMap.get(tailMap.firstKey());
    }
}
