package com.tt.core.net.proxy;

import com.tt.anno.TimeOut;
import com.tt.core.lb.LoadBalanceManager;
import com.tt.core.message.MessageEntry;
import com.tt.core.message.MessageManager;
import com.tt.core.net.handler.queuekey.IRpcQueueKeyAlgo;
import com.tt.core.net.handler.queuekey.RpcQueueKeyAlgoManager;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import com.tt.core.rpc.RpcFutureManager;
import com.tt.core.util.IDUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * rpc消息代理
 */
@Slf4j
public class RpcRequestProxy extends BaseMessageProxy {

    private final int serverId;

    public RpcRequestProxy(MessageManager messageManager, int serverId) {
        super(messageManager);
        this.serverId = serverId;
    }

    @Override
    public Object invoke(MessageEntry entry, Object proxy, Method method, Object[] args) throws Throwable {
        IRpcQueueKeyAlgo algo = RpcQueueKeyAlgoManager.getInstance().getAlgo(entry.getRpcQueueAlgoClz());
        var queueKey = algo.getQueueKey(entry, args);
        if (entry.isAsync()) {
            return asyncGetResult(method, entry, args, queueKey);
        }
        return getResult(method, entry, args, queueKey).get();
    }

    @Override
    protected MessageEntry getMessageEntry(Method method) {
        return messageManager.getMessageEntryByName(method.getName());
    }

    protected CompletableFuture<?> asyncGetResult(Method method, MessageEntry entry, Object[] args, int queueKey) {
        return getResult(method, entry, args, queueKey);
    }

    protected CompletableFuture<?> getResult(Method method, MessageEntry entry, Object[] args, int queueKey) {
        TimeOut timeOut = method.getAnnotation(TimeOut.class);
        long waitTime = timeOut == null ? 5 : timeOut.time();
        TimeUnit unit = timeOut == null ? TimeUnit.SECONDS : timeOut.unit();
        RpcMsg msg = new RpcMsg(entry, IDUtil.generateId(serverId), queueKey, args);
        // 每次重新获取
        Session session = getSession(msg);
        CompletableFuture<?> future = RpcFutureManager.getInstance().addFuture(IDUtil.generateId(serverId), waitTime, unit);
        session.writeAndFlush(msg);
        return future;
    }

    /**
     * 这里有个问题，无法使用绑定的关系处理
     *
     * @param msg
     * @return
     */
    protected Session getSession(RpcMsg msg) {
        return LoadBalanceManager.getInstance().getNetSession(null, msg.getEntry().lbType(), msg.getEntry().getId(), msg.getQueueKey());
    }
}
