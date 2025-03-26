package org.ainzson.oops.collections.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {

    private final BlockingQueue blockingQueue;

    public MessageQueue() {
        this.blockingQueue = new LinkedBlockingQueue(5);
    }

    public void producer(String message) throws InterruptedException {
        blockingQueue.put(message);
        System.out.println("📩 Produced: " + message);
    }

    public String consumer() throws InterruptedException {
        String message = (String) blockingQueue.take();
        System.out.println("📨 Consumed: " + message);
        return message;
    }

    public void start() {
        Thread producer = new Thread(()->{
           try {
               for (int i = 1; i <= 10; i++) {
                   this.producer("Message " + i);
                   Thread.sleep(500); // Simulate delay
               }
           }
           catch (InterruptedException exception)  {
               Thread.currentThread().interrupt();
           }
        });


        Thread consumer = new Thread(()->{
            try{
                for (int i = 1; i <= 10; i++) {
                    this.consumer();
                    Thread.sleep(1000); // Simulate processing delay
                }
            }
            catch (Exception exception) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
