package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {
    private final Queue<T> blockQueue = new LinkedList<>();
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue (T item)  {
        while (blockQueue.size() == capacity) {
            try {
                wait();
            }catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            finally {
                Thread.currentThread().interrupt();
            }
        }
        blockQueue.add(item);
        notifyAll();
    }

    public synchronized T dequeue () {
        while (blockQueue.isEmpty()){
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                Thread.currentThread().interrupt();
            }
        }
        T element = blockQueue.poll();
        notifyAll();
        return element;
    }

    public synchronized int size() {
        return blockQueue.size();
    }
}
