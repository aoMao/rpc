package com.tt.core.thread.queue;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 无锁队列，适合多生产者，单消费者情况，使用示例见{@link QueueExecutor}
 *
 * @param <T>
 */
public class NoLockQueue<T> {

    public static class Node<T> {
        private T value;
        private final AtomicReference<Node<T>> next = new AtomicReference<>();
    }

    private volatile Node<T> head;
    private volatile Node<T> tail;

    private final AtomicInteger size;
    private final int capacity;

    public NoLockQueue(int capacity) {
        tail = new Node<>();
        head = tail;
        this.capacity = capacity;
        size = new AtomicInteger();
    }

    public boolean isEmpty() {
        // 添加的时候先加的size，可能会出现判断不为空，但是获取的时候为空的情况，这里使用head的next是否为空判断是否队列是否为空
        return head.next.get() == null;
    }

    public int size() {
        return size.get();
    }

    public boolean add(T t) {
        // 容量限制
        if (size.incrementAndGet() > capacity) {
            size.decrementAndGet();
            return false;
        }

        Node<T> newNode = new Node<>();
        newNode.value = t;
        var curTail = tail;
        while (!curTail.next.compareAndSet(null, newNode)) {
            curTail = tail;
        }
        // 修改tail让其他线程执行add
        tail = newNode;
        return true;
    }

    public T poll() {
        var head = this.head;
        var next = head.next.get();
        if (next == null) {
            return null;
        }
        while (!head.next.compareAndSet(next, null)) {
            head = this.head;
            next = head.next.get();
        }
        if (next == null) {
            return null;
        }
        this.head = next;
        size.decrementAndGet();
        return next.value;
    }

    public static void main(String[] args) throws InterruptedException {
        int threadSize = 100;
        int queueSize = 20000000;
        NoLockQueue<Integer> integerNoLockQueue = new NoLockQueue<>(queueSize / 2);
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
        executorService.shutdown();
    }
}
