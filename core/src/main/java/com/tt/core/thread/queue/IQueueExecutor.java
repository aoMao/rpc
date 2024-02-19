package com.tt.core.thread.queue;

/**
 * 有序任务执行接口
 */
public interface IQueueExecutor {
    void execute(QueueTask t);
}
