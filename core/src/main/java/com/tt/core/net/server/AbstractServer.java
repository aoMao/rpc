package com.tt.core.net.server;

import com.tt.core.message.MessageManager;
import com.tt.core.net.handler.decode.ICustomMsgDecoder;
import com.tt.core.net.session.Session;
import com.tt.core.net.session.SessionManager;
import com.tt.core.thread.queue.IQueueExecutor;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;

import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
public abstract class AbstractServer<T extends AbstractBootstrap> {

    protected SessionManager sessionManager;
    protected IQueueExecutor executor;
    protected EventLoopGroup loopGroup;
    protected ICustomMsgDecoder customMsgDecoder;
    // 工作线程数量
    protected int workThreadCnt = Runtime.getRuntime().availableProcessors() * 2;
    private final int port;
    protected Consumer<Session> newSessionConnectConsumer;
    // 发送消息管理，用来初始化session的发消息代理
    protected MessageManager messageManager;

    public AbstractServer(int port) {
        this.port = port;
    }

    public AbstractServer<T> sessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        return this;
    }

    public AbstractServer<T> executor(IQueueExecutor executor) {
        this.executor = executor;
        return this;
    }

    public AbstractServer<T> customMsgDecoder(ICustomMsgDecoder customMsgDecoder) {
        this.customMsgDecoder = customMsgDecoder;
        return this;
    }

    public AbstractServer<T> workThreadCnt(int workThreadCnt) {
        this.workThreadCnt = workThreadCnt;
        return this;
    }

    public AbstractServer<T> newSessionConnectConsumer(Consumer<Session> newSessionConnectConsumer) {
        this.newSessionConnectConsumer = newSessionConnectConsumer;
        return this;
    }

    public AbstractServer<T> messageManager(MessageManager sendMessageManager) {
        this.messageManager = sendMessageManager;
        return this;
    }

    public void start() throws InterruptedException {
        loopGroup = createBossEventLoop();
        T bootstrap = createBootStrap();
        ChannelFuture f = bootstrap.bind(port)
                .sync();
        f.channel()
                .closeFuture()
                .addListener((c) -> close());
    }

    public int getPort() {
        return port;
    }

    public void close() {
        loopGroup.shutdownGracefully();
    }

    protected abstract EventLoopGroup createBossEventLoop();

    protected abstract T createBootStrap();

    protected abstract ChannelInitializer<Channel> createChannelInit();
}
