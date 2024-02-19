package com.tt.core.util;

import com.tt.core.thread.queue.QueueExecutor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class QueueExecutorUtil {

	/**
	 * 单线程执行任务
	 *
	 * @param queueSize
	 * @return
	 */
	public static QueueExecutor singleThreadQueueExecutor(int queueSize) {
		// LinkedBlockingDeque只需要有一个空位即可，保存当前任务提交的下一个有序任务，不会出现同时提交多个的情况
		var pool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.DAYS, new LinkedBlockingDeque<>(2));
		return new QueueExecutor(pool, queueSize);
	}

	/**
	 * 单线程执行任务
	 *
	 * @param queueSize
	 * @return
	 */
	public static QueueExecutor singleThreadQueueExecutor(int queueSize, ThreadFactory factory) {
		// LinkedBlockingDeque只需要有一个空位即可，保存当前任务提交的下一个有序任务，不会出现同时提交多个的情况
		var pool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.DAYS, new LinkedBlockingDeque<>(2), factory);
		return new QueueExecutor(pool, queueSize);
	}

	/**
	 * 单线程执行任务
	 *
	 * @param queueSize
	 * @return
	 */
	public static QueueExecutor singleThreadQueueExecutor(ThreadPoolExecutor pool, int queueSize) {
		return new QueueExecutor(pool, queueSize);
	}
}
