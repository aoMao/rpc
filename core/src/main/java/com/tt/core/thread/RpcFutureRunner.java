package com.tt.core.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * rpc回调
 */
public class RpcFutureRunner<R> implements Future<R>, RunnableFuture<R> {

    Lock lock = new ReentrantLock();
    Throwable cause;
    private volatile R result;

    AtomicInteger state = new AtomicInteger();

    public RpcFutureRunner() {
        state.set(RpcFutureState.NEW.getState());
    }

    @Override
    public void run() {

    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return state.get() == RpcFutureState.CANCEL.getState();
    }

    @Override
    public boolean isDone() {
        return state.get() == RpcFutureState.DONE.getState();
    }

    @Override
    public R get() throws InterruptedException, ExecutionException {
        if (await()) {
            return result;
        }
        if (cause != null) {
            throw new RuntimeException(cause);
        }
        return null;
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (await(timeout, unit)) {
            return result;
        }
        if (cause != null) {
            throw new RuntimeException(cause);
        }
        return null;
    }

    private boolean await() {
        try {
            lock.lock();
            lock.wait();
        } catch (InterruptedException e) {
            cause = e;
            state.set(RpcFutureState.EXCEPTION.getState());
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    private boolean await(long time, TimeUnit unit) {
        try {
            lock.lock();
            lock.wait(unit.toMillis(time));
        } catch (InterruptedException e) {
            cause = e;
            state.set(RpcFutureState.EXCEPTION.getState());
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    enum RpcFutureState {
        /** 新建 */
        NEW(0),
        /** 运行中 */
        RUN(1),
        /** 完成 */
        DONE(2),
        /** 取消 */
        CANCEL(3),
        /** 异常 */
        EXCEPTION(4),
        ;
        final int state;

        RpcFutureState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
