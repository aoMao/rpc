package com.tt.core.server.config;

import com.tt.message.constant.ServerType;
import com.tt.message.entity.server.ServerInfo;
import org.springframework.beans.factory.annotation.Value;

public class ServerInfoConfig {
	@Value("${server.self.ip}")
	private String ip;
	@Value("${server.self.port}")
	private int port;
	@Value("${server.self.listenTypes}")
	private ServerType[] listenTypes;
	private ServerType serverType;

	private ServerInfo serverInfo;

	public ServerInfoConfig(ServerType serverType) {
		this.serverType = serverType;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public ServerType[] getListenTypes() {
		return listenTypes;
	}

	public ServerInfo getServerInfo() {
		if (serverInfo == null) {
			synchronized (this) {
				if (serverInfo == null) {
					serverInfo = new ServerInfo(serverType, ip, port, listenTypes);
				}
			}
		}
		return serverInfo;
	}
}
