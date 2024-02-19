package com.tt.gate.config;

import com.tt.core.message.MessageManager;
import com.tt.core.net.proxy.CustomBeanDefinitionRegistryPostProcessor;
import com.tt.core.net.session.SessionManager;
import com.tt.core.net.util.MessageUtil;
import com.tt.core.server.RCClient;
import com.tt.core.server.config.ServerInfoConfig;
import com.tt.core.thread.queue.CurTaskExecutor;
import com.tt.core.thread.queue.IQueueExecutor;
import com.tt.message.constant.ServerType;
import com.tt.message.rpc.rc.IRcRpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    private MessageManager messageManager;

    @Bean
    public ServerInfoConfig serverInfoConfig() {
        return new ServerInfoConfig(ServerType.GATE);
    }

    @Bean
    public RCClient rcClient() {
        return new RCClient();
    }

    @Bean
    public SessionManager serverSessionManager() {
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
                    var clzSet = MessageUtil.getMessageClzSet(IRcRpc.class);
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


}
