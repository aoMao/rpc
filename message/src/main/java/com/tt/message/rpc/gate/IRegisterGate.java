package com.tt.message.rpc.gate;

import com.tt.message.entity.server.ServerInfo;

/**
 * 向gate注册
 */
public interface IRegisterGate extends IGateRpc {

    /**
     * 向gate注册
     *
     * @param serverInfo
     * @return
     */
    boolean register(ServerInfo serverInfo);
}
