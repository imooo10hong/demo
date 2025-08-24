package org.example.thread.demo;

import java.util.LinkedList;
import java.util.Queue;

// 调整生产耗时和消费耗时去研究wait()方法和notifyAll()方法
public class WaitNotifyDemo {

  private final Queue<Integer> queue = new LinkedList<>();
  private static final int CAPACITY = 5;

  public static void main(String[] args) {
    WaitNotifyDemo example = new WaitNotifyDemo();

    // 生产者线程
    Thread producer = new Thread(() -> {
      for (int i = 1; i <= 10; i++) {
        try {
          example.produce(i);
          Thread.sleep(100); // 模拟生产耗时
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });

    // 消费者线程
    Thread consumer = new Thread(() -> {
      for (int i = 0; i < 10; i++) {
        try {
          example.consume();
          Thread.sleep(200); // 模拟消费耗时
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });

    producer.start();
    consumer.start();
  }

  public void produce(int value) throws InterruptedException {
    synchronized (queue) {
      // 如果队列已满，等待消费者消费
      while (queue.size() == CAPACITY) {
        System.out.println("队列已满，生产者等待...");
        queue.wait(); // 释放锁并等待
      }

      queue.add(value);
      System.out.println("生产: " + value + "，队列大小: " + queue.size());

      // 通知消费者有新数据 唤醒所有等待线程
      queue.notifyAll();
    }
  }

  public void consume() throws InterruptedException {
    synchronized (queue) {
      // 如果队列为空，等待生产者生产
      while (queue.isEmpty()) {
        System.out.println("队列为空，消费者等待...");
        queue.wait(); // 释放锁并等待
      }

      int value = queue.poll();
      System.out.println("消费: " + value + "，队列大小: " + queue.size());

      // 通知生产者有空位 唤醒所有等待线程
      queue.notifyAll();
    }
  }
}
