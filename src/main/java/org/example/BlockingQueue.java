package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {
    private final Queue<T> blockQueue = new LinkedList<>();
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue (T item) throws InterruptedException {
        while (blockQueue.size() == capacity) {
            wait();
        }
        blockQueue.add(item);
        notifyAll();
    }

    public synchronized T dequeue () throws InterruptedException{
        while (blockQueue.isEmpty()){
            wait();
        }
        T element = blockQueue.poll();
        notifyAll();
        return element;
    }

    public synchronized int size() {
        return blockQueue.size();
    }
}
