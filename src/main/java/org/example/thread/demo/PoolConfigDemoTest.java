package org.example.thread.demo;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class PoolConfigDemoTest {

  ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 100, 100, TimeUnit.SECONDS,
      new LinkedBlockingDeque<>(100));

  @Test
  public void testErrorConfig() {
    for (int i = 0; i < 5; i++) {
      final int taskIndex = 1;
      executor.execute(new Runnable() {
        @Override
        public void run() {
          System.out.println("taskIndex" + taskIndex);
          try {
            Thread.sleep(Integer.MAX_VALUE); // 极端测试，无限睡眠
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      });
    }
    // 实例中的第一个任务是一个永远也没有办法完成的任务，所以其他的 4 个任务只能永远在阻塞队列中等待着。
    // 因为例子中的 corePoolSize 为 1，阻塞队列的大小为 100。
    // 按照线程创建的规则，需要等阻塞队列已满，才会去创建新的线程。例子中加入了 5 个任务，阻塞队列大小为 4(<100)，所以线程池的调度器不会去创建新的线程，后面的 4 个任务只能等待。
    for (int i = 0; ; i++) {
      //每隔1秒，输出线程池的工作任务数量、总计的任务数量
      System.out.println(
          "- activeCount:" + executor.getActiveCount() + " - taskCount:" + executor.getTaskCount());
    }

  }
}
