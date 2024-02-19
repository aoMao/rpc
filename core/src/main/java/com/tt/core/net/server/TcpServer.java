package com.tt.core.net.server;

import com.tt.core.net.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 服务器内部通信
 */
public class TcpServer extends AbstractServer<ServerBootstrap> {
    private EventLoopGroup workGroup;

    public TcpServer(int port) {
        super(port);
    }

    private void initWorkGroup() {
        workGroup = new NioEventLoopGroup(workThreadCnt);
    }

    @Override
    public void close() {
        super.close();
        workGroup.shutdownGracefully();
    }

    @Override
    protected EventLoopGroup createBossEventLoop() {
        return new NioEventLoopGroup();
    }

    @Override
    protected ServerBootstrap createBootStrap() {
        initWorkGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(loopGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createChannelInit());
        return bootstrap;
    }

    @Override
    protected ChannelInitializer<Channel> createChannelInit() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new LengthFieldBasedFrameDecoder(16 * 1024 * 1024, 4, 4, 12, 0));
                p.addLast(new ActiveHandler(sessionManager, newSessionConnectConsumer));
                p.addLast(new RpcMsgEncoder());
                p.addLast(new MsgDecoder(new RpcMsgDecoder(messageManager)));
                p.addLast(new RpcDispatchHandler(executor, sessionManager));
            }
        };
    }
}
