package com.tt.rc.runner;

import com.tt.core.net.server.TcpServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RCRunner implements ApplicationRunner {
	@Resource
	private TcpServer server;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		server.start();
		log.info("server start on port : {}", server.getPort());
	}
}
