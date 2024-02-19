package com.tt.core.lb.algorithm;

import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

public interface IBalanceAlgo {

    Session getNetSession(Session session, int msgId, int queueKey);

    LBType lbType();

    /**
     * 添加服务端连接
     *
     * @param session
     */
    void addServerSession(Session session);

    /**
     * 移除服务端连接
     *
     * @param session
     */
    void removeServerSession(Session session);
}
