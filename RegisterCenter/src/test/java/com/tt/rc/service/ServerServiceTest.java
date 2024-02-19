package com.tt.rc.service;

import com.tt.core.net.session.ServerNetSession;
import com.tt.core.net.util.SessionUtil;
import com.tt.message.constant.ServerType;
import com.tt.message.entity.server.ServerInfo;
import com.tt.rc.RegisterCenterServer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest(classes = {RegisterCenterServer.class})
public class ServerServiceTest {

	@Autowired
	private ServerService serverService;

	@Test
	public void Test() {
		SessionUtil.setSessionThreadLocal(new ServerNetSession(new NioServerSocketChannel(), 1));
		ServerInfo serverInfo = new ServerInfo(1, ServerType.GATE, "127.0.0.1", 8080, new ServerType[]{ServerType.HALL, ServerType.WORLD});
		ServerInfo serverInfo1 = new ServerInfo(2, ServerType.HALL, "127.0.0.1", 8080, new ServerType[]{ServerType.HALL, ServerType.WORLD});
		ServerInfo serverInfo2 = new ServerInfo(3, ServerType.WORLD, "127.0.0.1", 8080, new ServerType[]{ServerType.HALL, ServerType.WORLD});
		ServerInfo[] infos = serverService.registerServer(serverInfo1);
		System.out.println(Arrays.toString(infos));
		infos = serverService.registerServer(serverInfo2);
		System.out.println(Arrays.toString(infos));
		infos = serverService.registerServer(serverInfo);
		System.out.println(Arrays.toString(infos));
	}
}
