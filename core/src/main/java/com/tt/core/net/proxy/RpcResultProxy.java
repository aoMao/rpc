package com.tt.core.net.proxy;

import com.tt.core.message.MessageEntry;
import com.tt.core.message.MessageManager;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;
import com.tt.core.net.util.SessionThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

/**
 * rpc回复代理
 */
@Slf4j
public class RpcResultProxy {

    private final MessageManager messageManager;

    public RpcResultProxy(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public Object rpcSendResult(JoinPoint point, Object result) {
        MessageEntry entry = messageManager.getResultMessageEntryByName(point.getSignature().getName());
        if (entry == null) {
            return result;
        }
        RpcMsg requestMsg = SessionThreadLocalUtil.getMsg();
        RpcMsg msg = new RpcMsg(entry, requestMsg.getMsgSeq(), requestMsg.getQueueKey(), result);
        // 直接使用当前session即可
        Session session = SessionThreadLocalUtil.getSession();
        session.writeAndFlush(msg);
        return result;
    }
}
