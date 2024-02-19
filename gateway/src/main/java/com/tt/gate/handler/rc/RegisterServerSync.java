package com.tt.gate.handler.rc;

import com.tt.message.entity.server.ServerInfo;
import com.tt.message.rpc.rc.IServerRegisterSync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * registerServer同步消息
 */
@Slf4j
@Component
public class RegisterServerSync implements IServerRegisterSync {
    @Override
    public CompletableFuture<Void> serverOnline(ServerInfo serverInfo) {
        log.info("new serverOnline, serverInfo : {}", serverInfo);
        return returnAsyncObj(null);
    }
}
