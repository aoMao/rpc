package com.tt.core.net.session;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class AbstractSessionManager implements ISessionManager {
	private final Map<Channel, Session> channelToSessionMap = new ConcurrentHashMap<>();
	private final Map<Integer, Session> idToSessionMap = new ConcurrentHashMap<>();

	/**
	 * 移除处理
	 */
	private final List<Consumer<Session>> removeConsumers = new ArrayList<>();

	/**
	 * 注册连接
	 *
	 * @param channel
	 */
	public Session registerSession(Channel channel) {
		Session t = createSession(channel);
		registerSession(t);
		return t;
	}

	public void registerSession(Session t) {
		channelToSessionMap.put(t.getChannel(), t);
		idToSessionMap.put(t.getChannelId(), t);
	}

	/**
	 * 移除连接
	 *
	 * @param t
	 */
	public void removeSession(Session t) {
		channelToSessionMap.remove(t.getChannel());
		idToSessionMap.remove(t.getChannelId());
		removeConsumers.forEach(f -> f.accept(t));
	}

	public Session getSession(Channel channel) {
		return channelToSessionMap.get(channel);
	}

	public Session getSession(int channelId) {
		return idToSessionMap.get(channelId);
	}

	/**
	 * 移除连接
	 *
	 * @param channel
	 */
	public Session removeSession(Channel channel) {
		var session = channelToSessionMap.remove(channel);
		if (session != null) {
			idToSessionMap.remove(session.getChannelId());
			removeConsumers.forEach(f -> f.accept(session));
		}
		return session;
	}

	/**
	 * 移除连接
	 *
	 * @param id
	 */
	public Session removeSession(int id) {
		var session = idToSessionMap.remove(id);
		if (session != null) {
			channelToSessionMap.remove(session.getChannel());
			removeConsumers.forEach(f -> f.accept(session));
		}
		return session;
	}

	public void addRemoveFunction(Consumer<Session> function) {
		removeConsumers.add(function);
	}

	protected abstract Session createSession(Channel channel);
}
