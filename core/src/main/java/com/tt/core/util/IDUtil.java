package com.tt.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IDUtil {

	private static final Map<Integer, AtomicInteger> incrIdMap = new ConcurrentHashMap<>();

	public static long generateId(int key) {
		var down32 = incrIdMap.computeIfAbsent(key, k -> new AtomicInteger()).incrementAndGet();
		return (long) key << 32 | down32;
	}
}
