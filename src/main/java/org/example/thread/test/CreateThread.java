package org.example.thread.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateThread {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    log.info("{}:mainThread", Thread.currentThread().getName());

    // lambda写法：
    Thread t1 = new Thread(() -> {
      log.info("{}:thread1", Thread.currentThread().getName());
    });
    t1.start();

    Thread t2 = new Thread() {
      @Override
      public void run() {
        // 或者用this.getName()，但Thread.currentThread().getName()更清楚
        log.info("{}:thread2", Thread.currentThread().getName());
      }
    };
    t2.start();

    ThreadDemo thread3 = new ThreadDemo();
    thread3.start();

    // runnable的lambda简化：Runnable runnable = () -> log.info("{}:thread4", Thread.currentThread().getName());
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        log.info("{}:thread4", Thread.currentThread().getName());
      }
    };
    Thread thread4 = new Thread(runnable);
    thread4.start();

    Thread thread5 = new Thread(new RunnableTask());
    thread5.start();

    // 分隔符
    CallableTask callableTask = new CallableTask(); // Callable接口的实现类
    FutureTask<Long> futureTask = new FutureTask<Long>(callableTask); // 通过Callable接口的实现类构造一个FutureTask实例
    Thread thread6 = new Thread(futureTask, "callableTask"); //使用FutureTask实例作为Thread构造器的target入参，构造新的Thread线程实例
    thread6.start();
    Thread.sleep(500);
    log.info("{}:让子弹飞一会儿...", Thread.currentThread().getName());
    log.info("{}:获取并发任务的执行结果", Thread.currentThread().getName());
    log.info("{}:线程占用时间{}", thread6.getName(), futureTask.get());

  }

  static class ThreadDemo extends Thread {

    // super调用父类的构造方法，也可以调用父类被重写的方法
    public ThreadDemo() {
      super();
    }

    // 调用父类未被重写的方法
    public void run() {
      log.info("{}:thread3", Thread.currentThread().getName());
    }
  }

  static class RunnableTask implements Runnable {

    @Override
    public void run() {
      log.info("{}:thread5", Thread.currentThread().getName());
    }
  }

  // 创建一个Callable接口的实现类，实现其call方法
  static class CallableTask implements Callable<Long> {
    @Override
    public Long call() throws Exception {
      long startTime = System.currentTimeMillis();
      log.info("{}:callableTask线程运行开始", Thread.currentThread().getName());
      Thread.sleep(500);
      long endTime = System.currentTimeMillis();
      log.info("{}:callableTask线程运行结束", Thread.currentThread().getName());
      return endTime - startTime;
    }
  }
}
