package com.tt.rc.config;

import com.tt.core.constant.MessageConst;
import com.tt.core.message.MessageManager;
import com.tt.core.net.proxy.CurSessionSendMsgFactoryBean;
import com.tt.core.net.proxy.CustomBeanDefinitionRegistryPostProcessor;
import com.tt.core.net.session.ServerSessionManager;
import com.tt.core.net.util.MessageUtil;
import com.tt.core.thread.QueueExecutor;
import com.tt.core.util.QueueExecutorUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

	private MessageManager sendMessageManager;

	@Bean
	public ServerSessionManager sessionManager() {
		return new ServerSessionManager();
	}

	@Bean(name = "recoverMessageManager")
	public MessageManager recoverMessageManager() {
		return new MessageManager();
	}

	@Bean(name = "sendMessageManager")
	public MessageManager sendMessageManager() {
		if (sendMessageManager == null) {
			synchronized (this) {
				if (sendMessageManager == null) {
					sendMessageManager = new MessageManager();
				}
			}
		}
		return sendMessageManager;
	}

	@Bean
	public QueueExecutor queueExecutor() {
		return QueueExecutorUtil.singleThreadQueueExecutor(1024);
	}

	@Bean
	public CustomBeanDefinitionRegistryPostProcessor sendMsgPostProcessorDefinition() {
		var set = MessageUtil.getMessageClzSet(MessageConst.RC_RESP_MESSAGE_PACKAGE_NAME);
		return new CustomBeanDefinitionRegistryPostProcessor(set.toArray(new Class[0]), CurSessionSendMsgFactoryBean.class, false, sendMessageManager());
	}

}
