package org.example.thread.demo;

import cn.hutool.core.util.RandomUtil;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class FutureBySubmitDemoTest {

  //测试用例：获取异步调用的结果
  @Test
  public void testSubmit1() throws ExecutionException, InterruptedException {
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
    Future<Integer> future = pool.submit(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        //返回1～1000的随机数
        return RandomUtil.randomInt(1, 1000);
      }
    });

    // 调用者可以通过Future.get()方法获取异步执行的结果
    Integer result = future.get();
    System.out.println("异步执行的结果是:" + result);
    Thread.sleep(1000);
    pool.shutdown();
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

  //异步的执行目标类：执行过程中将发生异常
  static class TargetTaskWithError extends TargetTask {
    public void run() {
      super.run();
      throw new RuntimeException("Error from " + taskName);
    }
  }

  //测试用例：提交和执行
  @Test
  public void testSubmit2() {
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
    pool.execute(new TargetTaskWithError()); // 第一个
    Future<?> future = pool.submit(new TargetTaskWithError()); // 第二个

    try {
      //如果异常抛出，就会在调用Future.get()时传递给调用者
      if (future.get() == null) {
        //如果Future的返回值为null，那么任务完成
        System.out.println("任务完成");
      }
    } catch (Exception e) {
      System.out.println(e.getCause().getMessage());
    }
    pool.shutdown();
  }

}