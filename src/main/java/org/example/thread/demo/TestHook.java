package org.example.thread.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class TestHook {

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
  public void testHooks() throws InterruptedException {
    /*
      创建了一个ThreadPoolExecutor的匿名子类
      大括号{}内就是这个匿名子类的类体
      在创建实例的同时定义了这个子类
     */
    ExecutorService pool = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2))

    {
      //继承：调度器终止钩子
      @Override
      protected void terminated() {
        System.out.println("调度器已经终止!");
      }

      //继承：执行前钩子
      @Override
      protected void beforeExecute(Thread t, Runnable target) {
        System.out.println(target + "前钩被执行");
        super.beforeExecute(t, target);
      }

      //继承：执行后钩子
      @Override
      protected void afterExecute(Runnable target, Throwable t) {
        super.afterExecute(target, t);
        System.out.println(target + "后钩被执行");
      }
    };

    for (int i = 1; i <= 5; i++)
    {
      pool.execute(new TargetTask());
    }
    //等待1秒
    Thread.sleep(1000);
    System.out.println("关闭线程池");
    pool.shutdown();
  }
}