package com.tt.test.config;

import com.tt.core.message.MessageManager;
import com.tt.core.net.proxy.CustomBeanDefinitionRegistryPostProcessor;
import com.tt.core.net.proxy.RpcRequestProxyFactoryBean;
import com.tt.core.net.proxy.RpcResultProxy;
import com.tt.core.net.session.SessionManager;
import com.tt.core.net.util.MessageUtil;
import com.tt.core.server.config.ServerInfoConfig;
import com.tt.core.thread.queue.CurTaskExecutor;
import com.tt.core.thread.queue.IQueueExecutor;
import com.tt.core.thread.queue.QueueExecutor;
import com.tt.core.util.QueueExecutorUtil;
import com.tt.message.constant.ServerType;
import com.tt.message.rpc.IRpc;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class BeanConfig {

    private MessageManager messageManager;
    private RpcResultProxy rpcResultProxy;

    @Value("${server.self.id}")
    private int serverId;

    @Bean
    public ServerInfoConfig serverInfoConfig() {
        return new ServerInfoConfig(ServerType.RPC_CALLER);
    }

    @Bean
    public QueueExecutor executor() {
        return QueueExecutorUtil.singleThreadQueueExecutor(1024);
    }

    @Bean
    public SessionManager sessionManager() {
        return new SessionManager();
    }

    @Bean
    public IQueueExecutor createQueueExecutor() {
        return new CurTaskExecutor();
    }

    @Bean(value = "messageManager")
    public MessageManager messageManager() {
        if (messageManager == null) {
            synchronized (this) {
                if (messageManager == null) {
                    messageManager = new MessageManager();
                    var clzSet = MessageUtil.getMessageClzSet(IRpc.class);
                    try {
                        messageManager.registerMsg(clzSet);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return messageManager;
    }

    public RpcResultProxy rpcResultProxy() {
        if (rpcResultProxy == null) {
            synchronized (this) {
                if (rpcResultProxy == null) {
                    rpcResultProxy = new RpcResultProxy(messageManager());
                }
            }
        }
        return rpcResultProxy;
    }


    @Bean
    public CustomBeanDefinitionRegistryPostProcessor postProcessor() {
        MessageManager messageManager = messageManager();
        return new CustomBeanDefinitionRegistryPostProcessor(messageManager.getAllMsgClz(), RpcRequestProxyFactoryBean.class, messageManager);
    }

    @AfterReturning(returning = "result", pointcut = "execution(* com.tt.test.handler..*.*(..))")
    public void afterReturn(JoinPoint point, Object result) {
        rpcResultProxy().rpcSendResult(point, result);
    }
}
