package com.tt.core.server;

import com.tt.core.message.MessageManager;
import com.tt.core.net.client.TcpClient;
import com.tt.core.net.handler.RpcMsgDecoder;
import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.proxy.CurSessionSendResultProxy;
import com.tt.core.net.session.Session;
import com.tt.core.net.session.SessionManager;
import com.tt.core.net.util.SessionThreadLocalUtil;
import com.tt.core.server.config.ServerInfoConfig;
import com.tt.core.util.QueueExecutorUtil;
import com.tt.message.entity.server.ServerRegisterResp;
import com.tt.message.rpc.rc.IServerRegister;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Proxy;

@Slf4j
public class RCClient {

    private static final RpcMsg FIRST_MSG = new RpcMsg(null, 0, 0);

    @Value("${rc.server.ip}")
    private String ip;
    @Value("${rc.server.port}")
    private int port;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ServerInfoConfig serverInfoConfig;

    private IServerRegister serverRegister;

    private TcpClient client;

    /**
     * 初始化并注册
     *
     * @throws Exception
     */
    public void register() throws Exception {
        var serverInfo = serverInfoConfig.getServerInfo();
        if (client == null) {
            initClient();
        }
        log.info("connect rc server.... ip={}, port={}", ip, port);
        Channel channel = client.syncConnect();
        // 发送注册消息
        ServerRegisterResp serverRegisterResp = getServerRegister().registerServer(serverInfo);
        log.info("registerServerResp selfId={}, serverInfos={}", serverRegisterResp.getSelfServerId(), serverRegisterResp.getServerInfos());
        serverInfoConfig.getServerInfo().setId(serverRegisterResp.getSelfServerId());
        SessionThreadLocalUtil.clear();
    }

    private void initClient() throws Exception {
        var messageManager = new MessageManager();
        messageManager.registerMsg(IServerRegister.class);
        var sessionManager = new SessionManager();
        client = (TcpClient) new TcpClient(ip, port).messageManager(messageManager)
                .executor(QueueExecutorUtil.singleThreadQueueExecutor(128))
                .sessionManager(sessionManager)
                .customMsgDecoder(new RpcMsgDecoder(messageManager))
                .customConnectSuccessConsumer(session -> log.info("connect rc server success, will send registerServer msg"));
    }

    public IServerRegister getServerRegister() {
        if (serverRegister == null) {
            synchronized (this) {
                if (serverRegister == null) {
                    serverRegister = (IServerRegister) Proxy.newProxyInstance(IServerRegister.class.getClassLoader(), new Class[]{IServerRegister.class}, new CurSessionSendResultProxy(messageManager));
                }
            }
        }
        return serverRegister;
    }
}
