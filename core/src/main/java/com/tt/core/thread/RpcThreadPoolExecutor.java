package com.tt.core.thread;

import java.util.concurrent.*;

/**
 * rpc线程执行
 */
public class RpcThreadPoolExecutor extends ThreadPoolExecutor {

    public RpcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return super.newTaskFor(callable);
    }
}
