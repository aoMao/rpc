package com.tt.message.entity.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册回复
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerRegisterResp {
    private int selfServerId;
    private ServerInfo[] serverInfos;
}
