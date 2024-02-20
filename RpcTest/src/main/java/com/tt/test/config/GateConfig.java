package com.tt.test.config;

import com.tt.core.gatelb.GateLbManager;
import com.tt.core.message.MessageEntry;
import com.tt.core.message.MessageManager;
import com.tt.core.net.client.TcpClient;
import com.tt.core.net.handler.RpcMsgDecoder;
import com.tt.core.net.proxy.RpcFixSessionProxy;
import com.tt.core.net.proxy.RpcRequestProxy;
import com.tt.core.net.session.SessionManager;
import com.tt.core.server.config.ServerInfoConfig;
import com.tt.core.thread.queue.QueueExecutor;
import com.tt.message.entity.server.ServerInfo;
import com.tt.message.rpc.IRpc;
import com.tt.message.rpc.gate.IRegisterGate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 配置gate服务器，这里临时使用，后续需要引入nacos或zookeeper
 */
@Slf4j
@Component
public class GateConfig {

    @Value("${gate.server.list}")
    private String gateList;

    @Value("${server.self.id}")
    int serverId;
    @Autowired
    ServerInfoConfig serverInfoConfig;
    @Autowired
    QueueExecutor executor;
    @Autowired
    MessageManager messageManager;
    @Autowired
    SessionManager sessionManager;
    @Autowired
    List<IRpc> iRpcs;


    @PostConstruct
    public void connect() {
        try {
            buildCanDealMsg();
        } catch (Exception e) {
            log.error("build message error, e", e);
            throw new RuntimeException(e);
        }
        String[] split = gateList.split(";");
        for (String s : split) {
            String[] split1 = s.split(":");
            String ip = split1[0];
            int port = Integer.parseInt(split1[1]);
            TcpClient client = (TcpClient) new TcpClient(ip, port).messageManager(messageManager)
                    .sessionManager(sessionManager)
                    .customMsgDecoder(new RpcMsgDecoder(messageManager))
                    .executor(executor).customConnectSuccessConsumer(session -> {
                        IRegisterGate gate = (IRegisterGate) Proxy.newProxyInstance(IRegisterGate.class.getClassLoader(),
                                new Class[]{IRegisterGate.class}, new RpcFixSessionProxy(messageManager, serverId, session));
                        CompletableFuture<ServerInfo> register = gate.register(serverInfoConfig.getServerInfo());
                        register.whenComplete((serverInfo, throwable) -> {

                            if (throwable != null) {
                                log.error("register gate error, e : ", throwable);
                                session.close();
                                return;
                            }
                            log.info("register gate resp : {}", serverInfo);
                            if (serverInfo != null) {
                                session.setServerInfo(serverInfo);
                                GateLbManager.getInstance().addServerSession(session);
                            }
                        });
                    });
            client.connect();
        }
    }

    private void buildCanDealMsg() throws Exception {
        for (IRpc iRpc : iRpcs) {
            if (iRpc instanceof Proxy && Proxy.getInvocationHandler(iRpc) instanceof RpcRequestProxy proxy) {
                proxy.setServerId(serverId);
                continue;
            }
            messageManager.registerMsg(iRpc);
        }

        ServerInfo serverInfo = serverInfoConfig.getServerInfo();
        for (MessageEntry messageEntry : messageManager.getAllMsgEntry()) {
            if (messageEntry.canDeal()) {
                serverInfo.getCanDealMsgIds().add(messageEntry.getId());
            }
            serverInfo.getMsgIdToLBTypeMap().put(messageEntry.getId(), messageEntry.lbType());
        }
    }
}
