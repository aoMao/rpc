package com.tt.core.net.handler;

import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.SessionManager;
import com.tt.core.net.task.RpcMessageDealTask;
import com.tt.core.rpc.RpcFutureManager;
import com.tt.core.thread.queue.IQueueExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * rpc消息分发，根据key进行分发消息线程
 */
@Slf4j
public class RpcDispatchHandler extends SimpleChannelInboundHandler<RpcMsg> {
    private final IQueueExecutor executor;
    private final SessionManager sessionManager;

    public RpcDispatchHandler(IQueueExecutor executor, SessionManager sessionManager) {
        this.executor = executor;
        this.sessionManager = sessionManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg msg) throws Exception {
        var session = sessionManager.getSession(ctx.channel());
        if (session == null) {
            log.error("dispatch msg not found session by channel, maybe not register, channel: {}, msg: {}", ctx.channel(), msg);
            return;
        }
        if (msg.getEntry().isResultMsg()) {
            CompletableFuture<Object> future = RpcFutureManager.getInstance().remove(msg.getMsgSeq());
            if (future == null) {
                log.warn("receive result msg not fount wait future, maybe timeOut, msg is : {}", msg);
            } else {
                future.complete(msg.getParams()[0]);
            }
        } else {
            RpcMessageDealTask task = new RpcMessageDealTask(msg, session);
            executor.execute(task);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        log.error("channel exception, address : {}, e", ctx.channel().remoteAddress().toString(), cause);
    }
}
