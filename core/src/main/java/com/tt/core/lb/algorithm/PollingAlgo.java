package com.tt.core.lb.algorithm;

import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

/**
 * 轮询
 * copy on write
 */
public class PollingAlgo implements IBalanceAlgo {
    @Override
    public Session getNetSession(Session session, int msgId, int queueKey) {
        return null;
    }

    @Override
    public LBType lbType() {
        return LBType.POLLING;
    }

    @Override
    public void addServerSession(Session session) {

    }

    @Override
    public void removeServerSession(Session session) {

    }
}
