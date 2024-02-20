package com.tt.core.gatelb.algo;

import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import com.tt.message.constant.LBType;

/**
 * gate连接算法
 */
public interface IGateLbAlgo {

    /**
     * 根据消息获取session
     * @param msg
     * @return
     */
    Session getSession(RpcMsg msg);

    /**
     * 算法类型
     * @return
     */
    LBType lbType();

    /**
     * 新增session
     * @param session
     */
    void addServerSession(Session session);

    /**
     * 移除session
     * @param session
     */
    void removeServerSession(Session session);
}
