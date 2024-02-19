package com.tt.core.message;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Type;

public interface IMessageDecodeAndEncode<T> {

	/**
	 * 解码
	 *
	 * @param buf
	 * @return
	 */
	T decode(ByteBuf buf, Type clz);

	/**
	 * 编码
	 *
	 * @param t
	 */
	int encode(T t, ByteBuf buf, Type type);

}
