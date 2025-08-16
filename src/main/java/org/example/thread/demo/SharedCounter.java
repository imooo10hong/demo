package org.example.thread.demo;

//deepseek给出三种解决在多线程环境下，因为counter++操作不是原子操作，导致部分增量丢失。都尝试一下
//1.private static AtomicInteger counter = new AtomicInteger(0); counter++改为counter.incrementAndGet()
//2.private static final Object lock = new Object(); // 在线程的run方法中：synchronized (lock) {counter++;}
//3.private static final ReentrantLock lock = new ReentrantLock();// 在线程的run方法中：lock.lock();try {counter++;} finally {lock.unlock();}

public class SharedCounter {

  private static int counter = 0;
  private static final int THREAD_COUNT = 10;
  private static final int INCREMENTS_PER_THREAD = 1_000_000;

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[THREAD_COUNT];
    for (int i = 0; i < THREAD_COUNT; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
          counter++;
        }
      });
      threads[i].start();
    }
    // 等待所有线程执行完毕
    for (Thread t : threads) {
      t.join();
    }
    // 预期结果应该是10,000,000
    System.out.println("Expected result: " + (THREAD_COUNT * INCREMENTS_PER_THREAD));
    System.out.println("Actual result: " + counter);
  }
}