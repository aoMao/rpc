package com.tt.core.thread;

import com.tt.core.thread.queue.NoLockQueue;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class NoLockQueueTest {

    @Test
    public void Test() throws InterruptedException {
        int threadSize = 1;
        int queueSize = 5000000;
        NoLockQueue<Integer> integerNoLockQueue = new NoLockQueue<>(queueSize);
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        AtomicInteger atomicInteger = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(threadSize);
        Set<Integer> addSet = new ConcurrentSkipListSet<>();
        for (int i = 0; i < threadSize; i++) {
            executorService.execute(() -> {
                System.out.println("one add thread execute start");
                int l = atomicInteger.incrementAndGet();
                try {
                    while (l < queueSize) {
                        boolean add = integerNoLockQueue.add(l);
                        if (add) {
                            addSet.add(l);
                        }
                        l = atomicInteger.incrementAndGet();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    System.out.println(addSet.size());
                    System.exit(0);
                }
                latch.countDown();
                System.out.println("one add thread execute end");
            });
        }
        System.out.println("start wait add");
        latch.await();
        System.out.println("add end");
        Set<Integer> getSet = new ConcurrentSkipListSet<>();
        CountDownLatch getLatch = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            executorService.execute(() -> {
                Integer poll = integerNoLockQueue.poll();
                while (poll != null) {
                    getSet.add(poll);
                    poll = integerNoLockQueue.poll();
                }
                getLatch.countDown();
                System.out.println("one get thread execute end");
            });
        }
        System.out.println("start wait get");
        getLatch.await();
        System.out.println("get end");
        if (!addSet.containsAll(getSet)) {
            throw new RuntimeException("addSet not contains getSet");
        }
        if (!getSet.containsAll(addSet)) {
            throw new RuntimeException("getSet not contains addSet");
        }
        System.out.println("check success");
    }
}
