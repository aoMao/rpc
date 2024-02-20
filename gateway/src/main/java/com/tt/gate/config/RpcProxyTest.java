package com.tt.gate.config;

import com.tt.core.message.MessageManager;
import com.tt.core.net.proxy.RpcResultProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RpcProxyTest {

    @Autowired
    MessageManager messageManager;

    private RpcResultProxy rpcResultProxy;

    public RpcResultProxy rpcResultProxy() {
        if (rpcResultProxy == null) {
            synchronized (this) {
                if (rpcResultProxy == null) {
                    rpcResultProxy = new RpcResultProxy(messageManager);
                }
            }
        }
        return rpcResultProxy;
    }

    @AfterReturning(returning = "result", pointcut = "execution(* com.tt.gate.handler.server..*(..))")
    public void afterReturn(JoinPoint point, Object result) {
        rpcResultProxy().rpcSendResult(point, result);
    }
}
