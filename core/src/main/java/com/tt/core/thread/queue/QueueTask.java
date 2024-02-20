package com.tt.core.thread.queue;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 *
 */
@Slf4j
public abstract class QueueTask implements Runnable {
    private QueueExecutor executor;

    public void before() {

    }

    public void after() {

    }

    @Override
    public void run() {
        try {
            before();
            doAction();
            after();
        } catch (Throwable e) {
            log.error("QueueTask execute error, e", e);
        } finally {
            afterTaskRunEnd();
        }
    }

    public void afterTaskRunEnd() {
        if (executor != null) {
            executor.afterTaskRunEnd();
        }
    }

    public void setExecutor(QueueExecutor executor) {
        this.executor = executor;
    }

    public abstract void doAction() throws InvocationTargetException, IllegalAccessException;

    public Object getKey() {
        return null;
    }
}
