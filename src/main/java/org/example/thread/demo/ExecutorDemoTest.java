package org.example.thread.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class ExecutorDemoTest {

  public static final int SLEEP_GAP = 1000;

  //异步任务的执行目标类
  static class TargetTask implements Runnable {

    static AtomicInteger taskNum = new AtomicInteger(1);
    private final String taskName;

    public TargetTask() {
      taskName = "task-" + taskNum.get();
      taskNum.incrementAndGet();
    }

    public void run() {
      System.out.println("任务：" + taskName + " doing");
      System.out.println(taskName + " 运行结束.");
    }
  }

  // 需要看文档比较一下这四种创建线程池方式的差异，代码结果不能直接比较出来

  // 创建单线程化线程池
  @Test
  public void testSingleThreadExecutor() throws InterruptedException {
    ExecutorService pool = Executors.newSingleThreadExecutor();
    for (int i = 0; i < 5; i++) {
      pool.execute(new TargetTask()); // 提交不需要返回值的任务
      pool.submit(new TargetTask()); // 提交需要返回值的任务，返回Future对象
    }
    Thread.sleep(SLEEP_GAP);
    System.out.println("关闭线程池");
    pool.shutdown();
  }

  // 创建固定数量的线程池
  @Test
  public void testNewFixedThreadPool() throws InterruptedException {
    ExecutorService pool = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 5; i++) {
      pool.execute(new TargetTask());
      pool.submit(new TargetTask());
    }
    Thread.sleep(SLEEP_GAP);
    System.out.println("关闭线程池");
    pool.shutdown();
  }

  // 创建可缓存线程池
  @Test
  public void testNewCacheThreadPool() throws InterruptedException {
    ExecutorService pool = Executors.newCachedThreadPool();
    for (int i = 0; i < 5; i++)
    {
      pool.execute(new TargetTask());
      pool.submit(new TargetTask());
    }
    Thread.sleep(SLEEP_GAP);
    System.out.println("关闭线程池");
    pool.shutdown();
  }

  // 创建可调度线程池 周期性执行
  @Test
  public void testNewScheduledThreadPool() throws InterruptedException {
    ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
    for (int i = 0; i < 2; i++)
    {
      // 0表示首次执行任务的延迟时间，100表示每次执行任务的间隔时间
      // TimeUnit.MILLISECONDS是执行的时间间隔数值，单位为毫秒
      scheduled.scheduleAtFixedRate(new TargetTask(), 0, 100, TimeUnit.MILLISECONDS);
    }
    Thread.sleep(SLEEP_GAP);
    System.out.println("关闭线程池");
    scheduled.shutdown();
  }
}




