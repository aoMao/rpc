package com.tt.core.net.session;

import io.netty.channel.Channel;

import java.util.function.Consumer;

/**
 * session管理
 */
public interface ISessionManager {

    /**
     * 注册新session
     *
     * @param channel
     * @return
     */
    Session registerSession(Channel channel);

    /**
     * 注册新session
     *
     * @param session
     */
    void registerSession(Session session);

    /**
     * 移除session
     *
     * @param session
     */
    void removeSession(Session session);

    /**
     * 移除连接
     *
     * @param channel
     */
    Session removeSession(Channel channel);

    /**
     * 根据channelId移除session
     *
     * @param id
     */
    Session removeSession(int id);

    /**
     * 根据channel获取session
     *
     * @param channel
     * @return
     */
    Session getSession(Channel channel);

    /**
     * 根据channelId获取session
     *
     * @param channelId
     * @return
     */
    Session getSession(int channelId);

    /**
     * 移除后处理
     *
     * @param consumer
     */
    void addRemoveFunction(Consumer<Session> consumer);
}
