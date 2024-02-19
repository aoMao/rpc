package com.tt.rc.handler;

import com.tt.message.entity.server.ServerInfo;
import com.tt.message.msg.rc.req.IServerRegister;
import com.tt.message.msg.rc.resp.IServerRegisterResp;
import com.tt.rc.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerRegisterHandler implements IServerRegister {

	@Autowired
	private ServerService serverService;

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	IServerRegisterResp iServerRegisterResp;

	@Override
	public void registerServer(ServerInfo serverInfo) {
		ServerInfo[] serverInfos = serverService.registerServer(serverInfo);
		iServerRegisterResp.registerServerResp(serverInfo.getId(), serverInfos);
	}
}
