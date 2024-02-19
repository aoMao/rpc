package com.tt.core.net.handler;

import com.tt.core.net.handler.decode.ICustomMsgDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MsgDecoder extends ByteToMessageDecoder {

	private final ICustomMsgDecoder msgDecoder;

	public MsgDecoder(ICustomMsgDecoder msgDecoder) {
		this.msgDecoder = msgDecoder;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		msgDecoder.decode(ctx.channel(), in, out);
	}
}
