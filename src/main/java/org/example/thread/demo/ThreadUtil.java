package org.example.thread.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {

  public static void shutdownThreadPoolGracefully(ExecutorService threadPool) {
    // 若已经关闭则返回
    if (threadPool == null || threadPool.isTerminated()) {
      return;
    }
    try {
      threadPool.shutdown();   //拒绝接受新任务
    } catch (SecurityException | NullPointerException e) {
      return;
    }
    try {
      // 等待60秒，等待线程池中的任务完成执行
      if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
        // false 会走下面的逻辑
        // 调用 shutdownNow() 方法取消正在执行的任务
        threadPool.shutdownNow();
        // 再次等待60秒，如果还未结束，可以再次尝试，或者直接放弃
        if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
          System.out.println("线程池任务未正常执行结束");
        }
      }
    } catch (InterruptedException ie) {
      // 捕获异常，重新调用 shutdownNow() 方法
      threadPool.shutdownNow();
    }
    // 仍然没有关闭，循环关闭1000次，每次等待10毫秒
    if (!threadPool.isTerminated()) {
      try {
        for (int i = 0; i < 1000; i++) {
          if (threadPool.awaitTermination(10, TimeUnit.MILLISECONDS)) {
            break;
          }
          threadPool.shutdownNow();
        }
      } catch (Throwable e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
