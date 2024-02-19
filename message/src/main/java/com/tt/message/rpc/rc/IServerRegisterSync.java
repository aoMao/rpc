package com.tt.message.rpc.rc;

import com.tt.message.anno.Message;
import com.tt.message.entity.server.ServerInfo;
import com.tt.message.rpc.IRpc;

import java.util.concurrent.CompletableFuture;

import static com.tt.message.constant.MessageIdStartConst.REGISTER_SERVER_SYNC;

/**
 * 注册中心同步
 */
@Message(startId = REGISTER_SERVER_SYNC)
public interface IServerRegisterSync extends IRpc {

    CompletableFuture<Void> serverOnline(ServerInfo serverInfo);
}
