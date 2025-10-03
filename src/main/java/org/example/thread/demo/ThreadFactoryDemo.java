package org.example.thread.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class ThreadFactoryDemo {
  // 一个简单的线程工厂类
  static public class SimpleThreadFactory implements ThreadFactory {
    static AtomicInteger count = new AtomicInteger(1);
    //实现其唯一的创建线程的方法
    @Override
    public Thread newThread(Runnable target) {
      String threadName = "simple-thread-" + count.get();
      System.out.println("创建一个新线程，名称为：" + threadName);
      count.incrementAndGet();
      Thread thread = new Thread(target, threadName);
      // 设置为守护线程
      thread.setDaemon(true);
      return thread;
    }
  }

  //异步任务的执行目标类
  static class TargetTask implements Runnable {
    static AtomicInteger taskNum = new AtomicInteger(1);
    String taskName;
    public TargetTask() {
      taskName = "task-" + taskNum.get();
      taskNum.incrementAndGet();
    }
    public void run() {
      System.out.println("任务：" + taskName + " doing");
      System.out.println(taskName + " 运行结束.");
    }
  }

  @Test
  public void testThreadFactory () throws InterruptedException {
    // 使用自定义线程工厂快捷创建一个固定大小的线程池
    ExecutorService pool = Executors.newFixedThreadPool(2, new SimpleThreadFactory());
    // 新建池中的线程名称都不是默认的形式，是线程工厂更改后的形式。
    // 对比：
    // ExecutorService pool = Executors.newFixedThreadPool(2);
    for (int i = 0; i < 5; i ++)
    {
      pool.submit(new TargetTask());
    }
    // 等待10s
    Thread.sleep(10000);
    System.out.println("关闭线程池");
    pool.shutdown();
  }
}
