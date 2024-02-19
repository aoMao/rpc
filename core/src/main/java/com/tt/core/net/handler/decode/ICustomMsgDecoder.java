package com.tt.core.net.handler.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public interface ICustomMsgDecoder {

	void decode(Channel channel, ByteBuf in, List<Object> out) throws Exception;

	boolean isTransferMsgDecoder();
}
