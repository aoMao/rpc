package com.tt.core.net.client;

import com.tt.core.net.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class TcpClient extends AbstractClient {


    public TcpClient(String ip, int port) {
        super(ip, port);
    }

    @Override
    protected ChannelFuture connectTo() {
        Bootstrap bootstrap = new Bootstrap().group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(createChannelInit());
        return bootstrap.connect(ip, port);
    }

    @Override
    public ChannelInitializer<Channel> createChannelInit() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new LengthFieldBasedFrameDecoder(16 * 1024 * 1024, 4, 4, 12, 0));
                p.addLast(new ActiveHandler(sessionManager, connectSuccessConsumer));
                p.addLast(new RpcMsgEncoder());
                p.addLast(new MsgDecoder(new RpcMsgDecoder(messageManager)));
                p.addLast(new RpcDispatchHandler(executor, sessionManager));
            }
        };
    }
}
