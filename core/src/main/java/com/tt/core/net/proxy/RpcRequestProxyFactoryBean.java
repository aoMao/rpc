package com.tt.core.net.proxy;

import com.tt.core.message.MessageManager;

public class RpcRequestProxyFactoryBean<T> extends BaseSendMsgFactoryBean<T> {


    public RpcRequestProxyFactoryBean(Class<T> clz, MessageManager messageManager) {
        super(clz, messageManager);
    }

    @Override
    protected BaseMessageProxy createProxyObject() {
        return new RpcRequestProxy(messageManager);
    }
}
