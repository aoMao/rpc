package com.tt.message.rpc.gate;

import com.tt.message.anno.Message;
import com.tt.message.constant.MessageIdStartConst;
import com.tt.message.entity.server.ServerInfo;

import java.util.concurrent.CompletableFuture;

/**
 * 向gate注册
 */
@Message(startId = MessageIdStartConst.GATE)
public interface IRegisterGate extends IGateRpc {

    /**
     * 向gate注册
     *
     * @param serverInfo
     * @return
     */
    CompletableFuture<ServerInfo> register(ServerInfo serverInfo);
}
