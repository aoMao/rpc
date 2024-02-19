package com.tt.message.rpc.rc;

import com.tt.message.anno.Message;
import com.tt.message.constant.MessageIdStartConst;
import com.tt.message.entity.server.ServerInfo;
import com.tt.message.entity.server.ServerRegisterResp;

@Message(startId = MessageIdStartConst.REGISTER_SERVER)
public interface IServerRegister extends IRcRpc {
    /**
     * 服务上线
     *
     * @param serverInfo
     */
    ServerRegisterResp registerServer(ServerInfo serverInfo);

}
