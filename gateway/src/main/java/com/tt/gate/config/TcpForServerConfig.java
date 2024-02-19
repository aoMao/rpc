package com.tt.gate.config;

import com.tt.core.message.MessageManager;
import com.tt.core.net.server.TcpServer;
import com.tt.core.net.session.SessionManager;
import com.tt.core.thread.queue.IQueueExecutor;
import com.tt.gate.netty.codec.RpcMessageTransfer;
import com.tt.message.rpc.IRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;
import java.util.List;

@Configuration
public class TcpForServerConfig {

    @Value("${server.self.port}")
    private int serverPort;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    @Qualifier(value = "messageManager")
    private MessageManager messageManager;
    @Autowired
    private IQueueExecutor executorManager;
    @Autowired
    private List<IRpc> messageList;

    @Bean(name = "gateForServerTcpServer")
    public TcpServer gateForServerTcpServer() throws Exception {
        var messageManager = new MessageManager();
        registerMsg(messageManager);
        var rpcMessageTransfer = new RpcMessageTransfer(messageManager, sessionManager);
        return (TcpServer) new TcpServer(serverPort).sessionManager(sessionManager)
                .executor(executorManager)
                .customMsgDecoder(rpcMessageTransfer)
                .messageManager(this.messageManager);
    }

    private void registerMsg(MessageManager messageManager) throws Exception {
        for (IRpc message : messageList) {
            // 代理的不进行注入
            if (message instanceof Proxy) {
                return;
            }
            messageManager.registerMsg(message);
        }
    }
}
