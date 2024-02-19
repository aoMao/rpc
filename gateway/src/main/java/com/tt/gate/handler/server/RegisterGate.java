package com.tt.gate.handler.server;

import com.tt.core.lb.LoadBalanceManager;
import com.tt.core.net.session.Session;
import com.tt.core.net.util.SessionThreadLocalUtil;
import com.tt.message.entity.server.ServerInfo;
import com.tt.message.rpc.gate.IRegisterGate;
import org.springframework.stereotype.Component;

/**
 * 向gate注册自己可以处理的消息
 */
@Component
public class RegisterGate implements IRegisterGate {

    @Override
    public boolean register(ServerInfo serverInfo) {
        Session session = SessionThreadLocalUtil.getSession();
        session.setServerInfo(serverInfo);
        LoadBalanceManager.getInstance().addServerSession(session);
        return true;
    }
}
