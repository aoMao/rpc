package com.tt.message.constant;

public enum LBType {
	/**
	 * 轮询
	 */
	POLLING(false),
	/**
	 * 根据id绑定
	 */
	BIND_BY_ID,
	/**
	 * 一致性哈希
	 */
	CONSISTENT_HASH,
	/**
	 * 服务器id
	 */
	SERVER_ID,
	;

	/**
	 * 是否需要queueKey，序列化时使用
	 */
	final boolean needKey;

	LBType() {
		this.needKey = true;
	}

	LBType(boolean needKey) {
		this.needKey = needKey;
	}

	public boolean isNeedKey() {
		return needKey;
	}

	public static LBType getByIndex(int index) {
		return LBType.values()[index];
	}
}
