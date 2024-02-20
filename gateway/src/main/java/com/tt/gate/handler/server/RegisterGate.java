package com.tt.gate.handler.server;

import com.tt.core.net.session.Session;
import com.tt.core.net.util.SessionThreadLocalUtil;
import com.tt.core.server.config.ServerInfoConfig;
import com.tt.gate.lb.LoadBalanceManager;
import com.tt.message.entity.server.ServerInfo;
import com.tt.message.rpc.gate.IRegisterGate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 向gate注册自己可以处理的消息
 */
@Component
public class RegisterGate implements IRegisterGate {

    @Autowired
    ServerInfoConfig serverInfoConfig;

    @Override
    public CompletableFuture<ServerInfo> register(ServerInfo serverInfo) {
        Session session = SessionThreadLocalUtil.getSession();
        session.setServerInfo(serverInfo);
        LoadBalanceManager.getInstance().addServerSession(session);
        return CompletableFuture.completedFuture(serverInfoConfig.getServerInfo());
    }
}
