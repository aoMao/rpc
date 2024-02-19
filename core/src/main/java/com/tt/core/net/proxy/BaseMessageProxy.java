package com.tt.core.net.proxy;

import com.tt.core.message.MessageEntry;
import com.tt.core.message.MessageManager;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

/**
 * 消息代理
 */
@Slf4j
public abstract class BaseMessageProxy implements InvocationHandler {

    protected final MessageManager messageManager;

    public BaseMessageProxy(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        MessageEntry entry = getMessageEntry(method);
        if (entry == null) {
            log.error("send msg not found message entry, method name = {}", method.getName());
            return null;
        }
        return invoke(entry, proxy, method, args);
    }

    public abstract Object invoke(MessageEntry entry, Object proxy, Method method, Object[] args) throws Throwable;

    protected abstract MessageEntry getMessageEntry(Method method);

    protected abstract Session getSession(RpcMsg msg);
}
