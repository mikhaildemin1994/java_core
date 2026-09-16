package org.example;

import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueue<T> {
    private final Queue<T> blockQueue = new LinkedList<>();
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    @SneakyThrows
    public void enqueue (T item) {
        while (blockQueue.size() == capacity){
            wait();
        }
        blockQueue.add(item);
        notifyAll();
    }

    @SneakyThrows
    public T dequeue () {
        while (blockQueue.isEmpty()){
            wait();
        }
        T element = blockQueue.poll();
        notifyAll();
        return element;
    }

    public AtomicInteger size() {
        return new AtomicInteger(blockQueue.size());
    }
}
