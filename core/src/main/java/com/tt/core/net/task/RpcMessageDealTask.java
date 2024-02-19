package com.tt.core.net.task;

import com.tt.core.message.MessageEntry;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import com.tt.core.net.util.SessionThreadLocalUtil;
import com.tt.core.thread.queue.QueueTask;

import java.lang.reflect.InvocationTargetException;

/**
 * rpc消息处理
 */
public class RpcMessageDealTask extends QueueTask {

    private final RpcMsg msg;
    private final Session session;

    public RpcMessageDealTask(RpcMsg msg, Session session) {
        this.msg = msg;
        this.session = session;
    }

    @Override
    public void doAction() throws InvocationTargetException, IllegalAccessException {
        SessionThreadLocalUtil.setSessionThreadLocal(session);
        SessionThreadLocalUtil.setMsgThreadLocal(msg);
        MessageEntry entry = msg.getEntry();
        entry.getMethod().invoke(entry.getHandlerInstance(), msg.getParams());
        SessionThreadLocalUtil.clear();
    }

    @Override
    public Object getKey() {
        return msg.getQueueKey();
    }
}
