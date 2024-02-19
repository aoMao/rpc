package com.tt.core.net.session;

import com.tt.message.entity.server.ServerInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Objects;

/**
 * 服务器端session
 */
public class Session {
	private final Channel channel;
	private final int channelId;
	private String hashKey;

	private ServerInfo serverInfo;

	public Session(Channel channel, int channelId) {
		this.channel = channel;
		this.channelId = channelId;
	}

	public void write(Object msg) {
		channel.write(msg);
	}

	public void writeAndFlush(Object msg) {
		channel.writeAndFlush(msg);
	}

	public boolean isActive() {
		return channel.isOpen() && channel.isActive();
	}

	public ChannelFuture close() {
		return channel.close();
	}

	public Channel getChannel() {
		return channel;
	}

	public int getChannelId() {
		return channelId;
	}


	public int getServerId() {
		return serverInfo.getId();
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	public String routeKey() {
		if (serverInfo == null) {
			return null;
		}
		if (hashKey == null) {
			hashKey = String.format("%s_%d", serverInfo.getServerType().toString(), serverInfo.getId());
		}
		return hashKey;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Session that = (Session) o;
		return channelId == that.channelId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(channelId);
	}
}
