package com.tt.core.net.client;

import com.tt.core.message.MessageManager;
import com.tt.core.net.handler.decode.ICustomMsgDecoder;
import com.tt.core.net.session.Session;
import com.tt.core.net.session.SessionManager;
import com.tt.core.thread.queue.IQueueExecutor;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractClient {

    /**
     * 连接线程池
     */
    private static final ScheduledExecutorService connectThreadPool = Executors.newScheduledThreadPool(2);

    protected MessageManager messageManager;
    protected SessionManager sessionManager;
    protected IQueueExecutor executor;
    protected ICustomMsgDecoder customMsgDecoder;
    protected String ip;
    protected int port;
    protected int maxRetryTimes = 10;
    protected int retryTimes;
    protected Channel channel;
    protected EventLoopGroup loopGroup;
    protected Consumer<Session> connectSuccessConsumer = this::connectSuccess;
    protected Consumer<Session> customConnectSuccessConsumer;

    public AbstractClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initEventLoop();
    }

    public AbstractClient customConnectSuccessConsumer(Consumer<Session> customConnectSuccessConsumer) {
        this.customConnectSuccessConsumer = customConnectSuccessConsumer;
        return this;
    }

    public AbstractClient messageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
        return this;
    }

    public AbstractClient sessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        return this;
    }

    public AbstractClient executor(IQueueExecutor executor) {
        this.executor = executor;
        return this;
    }

    public AbstractClient customMsgDecoder(ICustomMsgDecoder customMsgDecoder) {
        this.customMsgDecoder = customMsgDecoder;
        return this;
    }

    public AbstractClient maxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
        return this;
    }

    public Channel syncConnect() {
        while (true) {
            try {
                ChannelFuture future = this.connectTo().sync();
                return future.channel();
            } catch (InterruptedException e) {
                log.error("connect to ip : {}, port : {} error, e : {}", ip, port, e);
            }
        }
    }

    public void connect() {
        connectThreadPool.execute(() -> this.connectTo().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("connect server success, ip={}, port={}", ip, port);
                channel = future.channel();
            } else {
                if (maxRetryTimes != 0 && ++retryTimes >= maxRetryTimes) {
                    log.error("reconnect time over than maxRetryTime, ip={}, port={}", ip, port);
                }
                connectThreadPool.schedule(this::connect, 3, TimeUnit.SECONDS);
            }
        }));
    }

    protected void connectSuccess(Session session) {
        if (customConnectSuccessConsumer != null) {
            customConnectSuccessConsumer.accept(session);
        }
        // 心跳定时
//		connectThreadPool.schedule(() -> {
//			var msg = new EncodeMsg(heartMsgEntry);
//			session.writeAndFlush(msg);
//		}, 15, TimeUnit.SECONDS);
    }

    protected abstract ChannelFuture connectTo();

    protected void initEventLoop() {
        loopGroup = new NioEventLoopGroup();
    }

    public void close() {
        if (channel != null) {
            channel.close();
            sessionManager.removeSession(channel);
        }
        if (loopGroup != null) {
            loopGroup.shutdownGracefully();
        }
    }

    public abstract ChannelInitializer<Channel> createChannelInit();
}
