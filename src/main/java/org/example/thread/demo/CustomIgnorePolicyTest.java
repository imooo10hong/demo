package org.example.thread.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class CustomIgnorePolicyTest {
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

  //自定义拒绝策略
  public static class CustomIgnorePolicy implements RejectedExecutionHandler
  {
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e)
    {
      System.out.println((r + " rejected; " + " - getTaskCount: " + e.getTaskCount()));
    }
  }

  @Test
  public void testCustomIgnorePolicy() throws InterruptedException {
    int corePoolSize = 2;          //核心线程数
    int maximumPoolSize = 4;       //最大线程数
    long keepAliveTime = 10;
    TimeUnit unit = TimeUnit.SECONDS;
    //最大排队任务数
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
    //线程工厂
    ThreadFactory threadFactory = new SimpleThreadFactory();
    //拒绝和异常处理策略
    RejectedExecutionHandler policy = new CustomIgnorePolicy();
    ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, policy);
    // 预启动所有核心线程
    pool.prestartAllCoreThreads();
    for (int i = 1; i <= 10; i++)
    {
      pool.execute(new TargetTask());
    }
    //等待10秒
    Thread.sleep(10000);
    System.out.println("关闭线程池");
    pool.shutdown();
  }
}
