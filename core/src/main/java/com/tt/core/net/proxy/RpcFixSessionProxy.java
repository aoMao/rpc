package com.tt.core.net.proxy;

import com.tt.core.message.MessageManager;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;

/**
 * rpc向特定session发消息
 */
public class RpcFixSessionProxy extends RpcRequestProxy {

    private final Session session;

    public RpcFixSessionProxy(MessageManager messageManager, int serverId, Session session) {
        super(messageManager, serverId);
        this.session = session;
    }

    @Override
    protected Session getSession(RpcMsg msg) {
        return session;
    }
}
