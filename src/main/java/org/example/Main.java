package org.example;

public class Main {
    public static void main( String[] args ) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new BlockingQueue<>(2);
        System.out.println("Стартовый размер очереди: " + blockingQueue.size());

        blockingQueue.enqueue("Add item 1");
        System.out.println("Размер очереди после добавления элемента: " +
                blockingQueue.size());

        blockingQueue.enqueue("Add item 2");
        System.out.println("Размер очереди после добавления второго элемента " +
                blockingQueue.size());

        Thread thread = new Thread(() -> {
            blockingQueue.dequeue();
            System.out.println("Размер очереди после удаления элемента: " +
                    blockingQueue.size());
        });
        thread.start();
    }
}
