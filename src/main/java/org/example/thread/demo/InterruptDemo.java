package org.example.thread.demo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterruptDemo {

  public static final int SLEEP_GAP = 5000;//睡眠时长
  public static int threadSeqNumber = 1;

  static class SleepThreadDemo1 extends Thread {

    public SleepThreadDemo1() {
      super("sleepThread-" + threadSeqNumber);
      threadSeqNumber++;
    }

    public void run() {
      try {
        log.info("{} 进入睡眠.", getName());
        // 线程睡眠5秒
        Thread.sleep(SLEEP_GAP);
      } catch (InterruptedException e) {
        e.printStackTrace(System.err);
        log.info("{} 发生被异常打断.", getName());
        return;
      }
      log.info("{} 运行结束.", getName());
    }

  }

  static class SleepThreadDemo2 extends Thread {

    public SleepThreadDemo2() {
      super("sleepThread-" + threadSeqNumber);
      threadSeqNumber++;
    }

    public void run() {
      try {
        // 线程睡眠5秒
        Thread.sleep(SLEEP_GAP);
      } catch (InterruptedException e) {
        e.printStackTrace(System.err);
        log.info("{} 先对线程中断后再阻塞，也会捕获异常.", getName());
        return;
      }
      log.info("{} 没有捕获异常", getName());
    }
  }

  static class SleepThreadDemo3 extends Thread {

    public SleepThreadDemo3() {
      super("sleepThread-" + threadSeqNumber);
    }

    public void run() {
      // 第一次调用阻塞方法
      try {
        Thread.sleep(SLEEP_GAP);
      } catch (InterruptedException e) {
        log.info("{} 第一次sleep被中断", getName());
        log.info("{} 捕获异常后，中断状态是:{}", getName(),
            Thread.currentThread().isInterrupted()); // false
      }

      // 第二次调用阻塞方法
      try {
        log.info("{} 准备第二次sleep...",  getName());
        Thread.sleep(SLEEP_GAP); // 这里不会再自动触发InterruptedException
        log.info("{} 第二次sleep顺利完成", getName()); // 如果没有新的中断，这句话会被打印
      } catch (InterruptedException e) {
        log.info("{} 第二次sleep被中断", getName()); // 除非在这2秒内线程又被中断了，否则不会执行这里
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    log.info("Demo1运行开始");
    Thread thread1 = new SleepThreadDemo1();
    thread1.start();
    Thread thread2 = new SleepThreadDemo1();
    thread2.start();

    // 主线程等待2秒
    Thread.sleep(2000);
    // 打断线程1
    thread1.interrupt();

    // 主线程等待5秒
    Thread.sleep(5000);
    //打断线程2，此时线程2已经终止，thread2.interrupt()中断操作没有产生实质性的效果
    thread2.interrupt();

    log.info("Demo1运行结束");

    // 线程的interrupt()方法先被调用，然后线程开始调用阻塞方法进入阻塞状态，InterruptedException 异常依旧会抛出
    log.info("Demo2运行开始");
    Thread.sleep(1000);
    Thread thread3 = new SleepThreadDemo2();
    thread3.interrupt();
    thread3.start();
    Thread.sleep(7000);
    log.info("Demo2运行结束");

    // 如果线程捕获InterruptedException异常后，继续调用阻塞方法，将不再触发InterruptedException异常。
    log.info("Demo3运行开始");
    Thread thread4 = new SleepThreadDemo3();
    thread4.start();
    Thread.sleep(1000);
    // 发起中断
    thread4.interrupt();
    Thread.sleep(15000);
    log.info("Demo3运行结束");
  }
}

