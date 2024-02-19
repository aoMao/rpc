package com.tt.core.net.proxy;

import com.tt.core.message.MessageManager;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public abstract class BaseSendMsgFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> clz;
    protected MessageManager messageManager;

    public BaseSendMsgFactoryBean(Class<T> clz, MessageManager messageManager) {
        this.clz = clz;
        this.messageManager = messageManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, createProxyObject());
    }

    protected abstract BaseMessageProxy createProxyObject();

    @Override
    public Class<?> getObjectType() {
        return clz;
    }
}
