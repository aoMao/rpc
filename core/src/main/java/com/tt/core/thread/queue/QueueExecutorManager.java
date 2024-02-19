package com.tt.core.thread.queue;

import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 */
public class QueueExecutorManager implements IQueueExecutor {

    /**
     * 使用数组方式，hash求余，这样不会频繁创建多余的对象，并且不需要进行删除
     * 数组数量需要保证多于poolExecutor的最大线程池数量，防止线程饥饿问题
     */
    private final QueueExecutor[] executors;

    public QueueExecutorManager(ThreadPoolExecutor poolExecutor, int queueSize) {
        int arrayLength = poolExecutor.getMaximumPoolSize() << 3;
        executors = new QueueExecutor[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            executors[i] = new QueueExecutor(poolExecutor, queueSize);
        }
    }

    /**
     * 根据key放到固定线程执行
     *
     * @param task
     * @param key
     */
    public <T> void execute(QueueTask task, T key) {
        int hashCode = key.hashCode();
        var executor = executors[hashCode % executors.length];
        executor.execute(task);
    }

    @Override
    public void execute(QueueTask t) {
        execute(t, t.getKey());
    }
}
