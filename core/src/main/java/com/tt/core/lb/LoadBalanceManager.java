package com.tt.core.lb;

import com.tt.core.lb.algorithm.BindAlgo;
import com.tt.core.lb.algorithm.ConsistentHashAlgo;
import com.tt.core.lb.algorithm.IBalanceAlgo;
import com.tt.core.lb.algorithm.ServerIdAlgo;
import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

import java.util.HashMap;
import java.util.Map;

/**
 * 路由规则管理
 */
public class LoadBalanceManager {

    private static final class Inner {
        private static final LoadBalanceManager instance = new LoadBalanceManager();
    }

    private LoadBalanceManager() {
        addLbBalanceAlgo(new ConsistentHashAlgo());
        addLbBalanceAlgo(new BindAlgo());
        addLbBalanceAlgo(new ServerIdAlgo());
    }

    public static LoadBalanceManager getInstance() {
        return Inner.instance;
    }

    private final Map<LBType, IBalanceAlgo> lbTypeIBalanceAlgoMap = new HashMap<>();


    public Session getNetSession(Session clientSession, int msgId, int queueKey) {
        // 根据每个连接传的规则进行处理
        LBType type = clientSession.getServerInfo().getMsgIdToLBTypeMap().getOrDefault(msgId, LBType.CONSISTENT_HASH);
        return getNetSession(clientSession, type, msgId, queueKey);
    }

    public Session getNetSession(Session clientSession, LBType lbType, int msgId, int queueKey) {
        // 根据每个连接传的规则进行处理
        var algo = lbTypeIBalanceAlgoMap.get(lbType);
        return algo.getNetSession(clientSession, msgId, queueKey);
    }

    public void addLbBalanceAlgo(IBalanceAlgo algo) {
        lbTypeIBalanceAlgoMap.put(algo.lbType(), algo);
    }


    public void addServerSession(Session session) {
        for (IBalanceAlgo algo : lbTypeIBalanceAlgoMap.values()) {
            algo.addServerSession(session);
        }
    }

    public void removeServerSession(Session session) {
        for (IBalanceAlgo algo : lbTypeIBalanceAlgoMap.values()) {
            algo.removeServerSession(session);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends IBalanceAlgo> T getAlgo(LBType lbType) {
        return (T) lbTypeIBalanceAlgoMap.get(lbType);
    }
}
