package com.tt.core.net.handler;

import com.tt.core.net.session.Session;
import com.tt.core.net.session.SessionManager;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class ActiveHandler extends ChannelDuplexHandler {

    private final SessionManager sessionManager;
    private final Consumer<Session> newSessionConnectConsumer;

    public ActiveHandler(SessionManager sessionManager, Consumer<Session> newSessionConnectConsumer) {
        this.sessionManager = sessionManager;
        this.newSessionConnectConsumer = newSessionConnectConsumer;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        var session = sessionManager.registerSession(ctx.channel());
        log.info("new channel active, address : {}", ctx.channel().remoteAddress().toString());
        if (newSessionConnectConsumer != null) {
            newSessionConnectConsumer.accept(session);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        sessionManager.removeSession(ctx.channel());
        log.info("channel inactive, address : {}", ctx.channel().remoteAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("channel exception, address : {}", ctx.channel().remoteAddress().toString());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event && event.state() == IdleState.READER_IDLE) {
            log.info("channel userEventTriggered, address={}, evt={}", ctx.channel().remoteAddress().toString(), evt);
            ctx.close();
        }
    }
}
