package com.tt.gate.service;

import com.tt.core.lb.LoadBalanceManager;
import com.tt.core.net.session.Session;
import org.springframework.stereotype.Component;

@Component
public class ServerService {

    public void serverDisconnect(Session session) {
        LoadBalanceManager.getInstance().removeServerSession(session);
    }
}
