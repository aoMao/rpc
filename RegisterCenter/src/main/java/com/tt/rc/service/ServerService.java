package com.tt.rc.service;

import com.tt.core.net.session.Session;
import com.tt.core.net.session.SessionManager;
import com.tt.core.net.util.SessionThreadLocalUtil;
import com.tt.core.thread.queue.IQueueExecutor;
import com.tt.core.thread.queue.QueueTask;
import com.tt.message.constant.ServerType;
import com.tt.message.entity.server.ServerInfo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ServerService {

    /**
     * id生成，自增
     */
    private final AtomicInteger id = new AtomicInteger(0);
    private final Map<ServerType, Map<Integer, ServerInfo>> typeServerInfoMap = new ConcurrentHashMap<>();
    /**
     * 监听某一个type的serverInfo列表
     */
    private final Map<ServerType, Set<Integer>> typeListenSetMap = new ConcurrentHashMap<>();

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private IQueueExecutor executor;

    @PostConstruct
    public void init() {
        sessionManager.addRemoveFunction(this::onServerDisconnect);
    }

    public ServerInfo[] registerServer(ServerInfo serverInfo) {
        log.info("new server register, serverInfo={}", serverInfo);
        var session = SessionThreadLocalUtil.getSession();
        serverInfo.setId(id.incrementAndGet());
        session.setServerInfo(serverInfo);
        sessionManager.registerServerSession(session);
        List<ServerInfo> infos = new ArrayList<>();
        // 保存下来
        for (ServerType listenType : serverInfo.getListenTypes()) {
            var map = typeServerInfoMap.get(listenType);
            if (map != null) {
                infos.addAll(map.values());
            }
            typeListenSetMap.computeIfAbsent(listenType, k -> new ConcurrentSkipListSet<>())
                    .add(serverInfo.getId());
        }
        typeServerInfoMap.computeIfAbsent(serverInfo.getServerType(), k -> new ConcurrentHashMap<>())
                .put(serverInfo.getId(), serverInfo);
        // 发送新服务上线通知
        var listenIds = typeListenSetMap.get(serverInfo.getServerType());
        if (listenIds != null) {
            var sessions = listenIds.stream()
                    .map(id -> sessionManager.getSessionByServerId(id))
                    .filter(Objects::nonNull)
                    .toList();
            for (Session session1 : sessions) {
                session1.getMsgProxy(IServerRegisterResp.class)
                        .newServerOnline(serverInfo);
            }
        }

        return infos.toArray(new ServerInfo[0]);
    }

    /**
     * 服务断开连接时，转移到业务执行线程执行，防止刚注册就断开出现没有删除问题
     *
     * @param session
     */
    public void onServerDisconnect(Session session) {
        executor.execute(new QueueTask() {
            @Override
            public void doAction() {
                // 没有设置serverInfo代表只是建立连接，并没有发送注册消息，无法知道对应类型
                if (session.getServerInfo() != null) {
                    removeServer(session.getServerInfo());
                }
            }
        });
    }

    /**
     * 移除连接，不发送通知，服务断开由其他服务器自行判断
     *
     * @param serverInfo
     */
    private void removeServer(ServerInfo serverInfo) {
        for (ServerType listenType : serverInfo.getListenTypes()) {
            var set = typeListenSetMap.get(listenType);
            if (set == null) {
                continue;
            }
            set.remove(serverInfo.getId());
        }
        var map = typeServerInfoMap.get(serverInfo.getServerType());
        if (map != null) {
            map.remove(serverInfo.getId());
        }
    }
}
