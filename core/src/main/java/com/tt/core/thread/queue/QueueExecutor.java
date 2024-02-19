package com.tt.core.thread.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Slf4j
public class QueueExecutor implements IQueueExecutor {

    private final ExecutorService executor;
    private final NoLockQueue<QueueTask> queue;
    private final AtomicBoolean runningState;
    private final AtomicInteger runCnt;

    public QueueExecutor() {
        this(128);
    }

    public QueueExecutor(int queueSize) {
        this(null, queueSize);
    }

    public QueueExecutor(ExecutorService executor, int queueSize) {
        if (executor == null) {
            int threadSize = Runtime.getRuntime().availableProcessors() * 2;
            executor = new ThreadPoolExecutor(threadSize, threadSize, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(threadSize));
        }
        this.executor = executor;
        runningState = new AtomicBoolean(false);
        queue = new NoLockQueue<>(queueSize);
        runCnt = new AtomicInteger();
    }

    @Override
    public void execute(QueueTask t) {
        t.setExecutor(this);
        if (!queue.add(t)) {
            log.error("add task error, t: {}", t);
            return;
        }
        if (runCnt.incrementAndGet() == 1) {
            tryExecute();
        }
    }

    public void afterTaskRunEnd() {
        runCnt.decrementAndGet();
        QueueTask poll = queue.poll();
        if (poll != null) {
            executor.execute(poll);
        } else {
            runningState.compareAndSet(true, false);
        }
    }

    /**
     * 尝试执行，可能会有当前已经在执行状态，但是之前的任务执行完毕还没有修改runningState的情况
     */
    private void tryExecute() {
        if (runningState.compareAndSet(false, true)) {
            // 这里必定可以获取到，这里如果可以执行，则状态必定修改完毕，不会有其他线程再去poll了
            executor.execute(queue.poll());
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            while (!executor.awaitTermination(10, TimeUnit.SECONDS)) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    public static void main(String[] args) throws InterruptedException {
        int max = 40000;
        QueueExecutor executor1 = new QueueExecutor(max);
        AtomicInteger cnt = new AtomicInteger();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    executor1.execute(new QueueTask() {
                        @Override
                        public void doAction() {
                            int j = cnt.incrementAndGet();
                            System.out.println(j);
                        }
                    });
                }
            }).start();
        }
    }

    public static class AddTask extends QueueTask {
        private final int j;
        private final Set<Integer> set;
        private final CountDownLatch latch;
        static volatile int i = 0;

        public AddTask(int j, Set<Integer> set, CountDownLatch latch) {
            this.j = j;
            this.set = set;
            this.latch = latch;
        }

        @Override
        public void doAction() {
            set.add(j);
            //            latch.countDown();
            ++i;
        }

        @Override
        public Object getKey() {
            return 1;
        }
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
