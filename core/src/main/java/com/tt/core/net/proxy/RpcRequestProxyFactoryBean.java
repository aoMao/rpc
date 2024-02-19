package com.tt.core.net.proxy;

import com.tt.core.message.MessageManager;

public class RpcRequestProxyFactoryBean<T> extends BaseSendMsgFactoryBean<T> {

    private final int serverId;

    public RpcRequestProxyFactoryBean(Class<T> clz, MessageManager messageManager, int serverId) {
        super(clz, messageManager);
        this.serverId = serverId;
    }

    @Override
    protected BaseMessageProxy createProxyObject() {
        return new RpcRequestProxy(messageManager, serverId);
    }
}
