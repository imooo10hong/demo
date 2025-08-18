package org.example.thread.demo;

import java.util.ArrayList;
import java.util.List;

// 一个线程状态的简单演示案例
public class StatusDemo {

  // 每个线程执行的轮次
  public static final long MAX_TURN = 5;

  // 线程编号
  static int threadSeqNumber = 1;

  // 全局的静态线程列表
  static List<Thread> threads = new ArrayList<>();

  // 输出静态线程列表中每个线程的状态
  private static void printThreadStatus(String curThreadName)
  {
    for (Thread t : threads)
    {
      System.out.println(t.getName() + "状态为：" + t.getState() + "------当前线程的名称" + curThreadName);
    }
  }

  // 向全局的静态线程列表中加入线程
  private static void addThread(Thread t)
  {
    threads.add(t);
  }

  static class StatusDemoThread extends Thread {
    public StatusDemoThread() {
      super("StatusDemoThread" + (threadSeqNumber ++));
      // 把自己加入全局的静态线程列表
      addThread(this);
    }
    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName() + "状态为：" + Thread.currentThread().getState());
      for (int i = 0; i < MAX_TURN; i++) {
        // 线程睡眠
        try {
          sleep(500);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        // 输出所有线程的状态
        printThreadStatus(Thread.currentThread().getName());
      }
      System.out.println(Thread.currentThread().getName() + "运行结束");
    }

    public static void main(String[] args) throws InterruptedException {
      // 将 main 线程加入全局线程列表
      addThread(Thread.currentThread());
      // 新建三个线程列表，它们在构造器中会将自己加入全局线程列表
      Thread t1 = new StatusDemoThread();
      System.out.println(t1.getName() + "状态为：" + t1.getState());
      Thread t2 = new StatusDemoThread();
      System.out.println(t2.getName() + "状态为：" + t2.getState());
      Thread t3 = new StatusDemoThread();
      System.out.println(t3.getName() + "状态为：" + t3.getState());

      t1.start();

      // 等待500ms再启动第二个线程
      sleep(500);
      t2.start();

      // 等待1000ms再启动第三个线程
      sleep(1000);
      t3.start();

      // 睡眠10s
      sleep(10000);
    }
  }
}
