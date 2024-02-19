package com.tt.core.net.proxy;

import com.tt.core.message.MessageManager;

/**
 * rpc 回复创建工厂
 *
 * @param <T>
 */
public class CurSessionSendMsgFactoryBean<T> extends BaseSendMsgFactoryBean<T> {

    public CurSessionSendMsgFactoryBean(Class<T> clz, MessageManager messageManager) {
        super(clz, messageManager);
    }

    @Override
    protected BaseMessageProxy createProxyObject() {
        return new CurSessionSendResultProxy(messageManager);
    }
}
