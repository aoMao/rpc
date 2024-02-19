package com.tt.core.net.proxy;

import com.tt.core.message.MessageEntry;
import com.tt.core.message.MessageManager;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import com.tt.core.net.util.SessionThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 向{@link SessionThreadLocalUtil}中的当前线程的session发消息代理
 */
@Slf4j
public class CurSessionSendResultProxy extends BaseMessageProxy {

    public CurSessionSendResultProxy(MessageManager messageManager) {
        super(messageManager);
    }

    @Override
    public Object invoke(MessageEntry entry, Object proxy, Method method, Object[] args) {
        Session curSession = SessionThreadLocalUtil.getSession();
        RpcMsg reqMsg = SessionThreadLocalUtil.getMsg();
        RpcMsg msg = new RpcMsg(entry, reqMsg.getMsgSeq(), reqMsg.getQueueKey(), args);
        curSession.writeAndFlush(msg);
        return null;
    }

    @Override
    protected MessageEntry getMessageEntry(Method method) {
        return messageManager.getMessageEntryByName(method.getName());
    }

    @Override
    protected Session getSession(RpcMsg msg) {
        return SessionThreadLocalUtil.getSession();
    }
}
