package com.tt.core.thread.queue;

/**
 * 当前线程执行
 */
public class CurTaskExecutor implements IQueueExecutor {
    @Override
    public void execute(QueueTask t) {
        t.run();
    }
}
