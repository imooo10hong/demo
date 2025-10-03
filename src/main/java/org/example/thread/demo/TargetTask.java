package org.example.thread.demo;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;

@Getter
public class TargetTask implements Runnable{
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
