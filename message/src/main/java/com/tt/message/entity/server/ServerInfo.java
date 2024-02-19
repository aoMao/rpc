package com.tt.message.entity.server;

import com.tt.message.constant.LBType;
import com.tt.message.constant.ServerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 服务信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {
    private int id;
    private ServerType serverType;
    private String ip;
    private int port;
    private ServerType[] listenTypes;
    Map<Integer, LBType> msgIdToLBTypeMap;
    List<Integer> canDealMsgIds;

    public ServerInfo(ServerType serverType, String ip, int port, ServerType[] listenTypes) {
        this.serverType = serverType;
        this.ip = ip;
        this.port = port;
        this.listenTypes = listenTypes;
    }
}
