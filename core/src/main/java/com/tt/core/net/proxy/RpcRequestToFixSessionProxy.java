package com.tt.core.net.proxy;

import com.tt.core.message.MessageManager;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;

/**
 * rpc固定session发送
 */
public class RpcRequestToFixSessionProxy extends RpcRequestProxy {

    private final Session session;

    public RpcRequestToFixSessionProxy(MessageManager messageManager, int serverId, Session session) {
        super(messageManager, serverId);
        this.session = session;
    }

    @Override
    protected Session getSession(RpcMsg msg) {
        return session;
    }
}
