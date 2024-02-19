package com.tt.rc.config;

import com.tt.core.message.MessageManager;
import com.tt.core.net.handler.decode.NormalMsgDecoder;
import com.tt.core.net.server.TcpServer;
import com.tt.core.net.session.ServerSessionManager;
import com.tt.core.thread.QueueExecutor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

	@Value("${server.port}")
	int port;

	@Autowired
	private ServerSessionManager sessionManager;
	@Autowired
	@Qualifier(value = "recoverMessageManager")
	private MessageManager messageManager;
	@Autowired
	@Qualifier(value = "sendMessageManager")
	private MessageManager sendMessageManager;
	@Resource
	private QueueExecutor queueExecutor;

	@Bean
	public TcpServer tcpServer() {
		return (TcpServer) new TcpServer(port).customMsgDecoder(new NormalMsgDecoder(messageManager, sessionManager))
											  .executor(queueExecutor).sessionManager(sessionManager)
											  .sendMessageManager(sendMessageManager);
	}
}
